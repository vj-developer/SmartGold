package com.greymatter.smartgold.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.greymatter.smartgold.R;
import com.greymatter.smartgold.adapter.ProductsAdapter;
import com.greymatter.smartgold.model.ProductListResponse;
import com.greymatter.smartgold.retrofit.APIInterface;
import com.greymatter.smartgold.retrofit.ApiConfig;
import com.greymatter.smartgold.retrofit.RetrofitBuilder;
import com.greymatter.smartgold.utils.Constants;
import com.greymatter.smartgold.utils.MyFunctions;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilteredProductsActivity extends AppCompatActivity {

    RecyclerView product_recycler;
    ProductsAdapter productAdapter;
    String from_price,to_price,order_by,category_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_products);

        product_recycler = findViewById(R.id.recyclerviewproduct);

        if (getIntent().getStringExtra(Constants.SEARCH_TERM).equals("null")){

            category_id = getIntent().getStringExtra(Constants.CATEGORY_ID);
            from_price = getIntent().getStringExtra(Constants.FROM_PRICE_RANGE);
            to_price = getIntent().getStringExtra(Constants.TO_PRICE_RANGE);
            order_by = getIntent().getStringExtra(Constants.ORDER_BY);

            filterProducts();

        }else {
            searchProduct(getIntent().getStringExtra(Constants.SEARCH_TERM));
        }




        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void searchProduct(String query) {
        MyFunctions.showLoading(FilteredProductsActivity.this);
        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);
        Call<ProductListResponse> call = apiInterface.search_products(ApiConfig.SecurityKey,Constants.AccessKeyVal,query);
        call.enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(Call<ProductListResponse> call, @NotNull Response<ProductListResponse> response) {
                MyFunctions.cancelLoading();

                if (response.isSuccessful()){

                    ProductListResponse productResponse = response.body();

                    if(productResponse.getSuccess()){

                        findViewById(R.id.order_empty).setVisibility(View.GONE);
                        product_recycler.setVisibility(View.VISIBLE);

                        productAdapter = new ProductsAdapter(productResponse.getData(),getApplicationContext());
                        product_recycler.setAdapter(productAdapter);
                    }else {

                        findViewById(R.id.order_empty).setVisibility(View.VISIBLE);
                        product_recycler.setVisibility(View.GONE);
                    }

                }else {

                    findViewById(R.id.order_empty).setVisibility(View.VISIBLE);
                    product_recycler.setVisibility(View.GONE);
                }


            }

            @Override
            public void onFailure(Call<ProductListResponse> call, Throwable t) {
                MyFunctions.cancelLoading();
                Toast.makeText(getApplicationContext(), Constants.API_ERROR, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void filterProducts() {
        MyFunctions.showLoading(FilteredProductsActivity.this);
        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);
        Call<ProductListResponse> call = apiInterface.filter_products(ApiConfig.SecurityKey,Constants.AccessKeyVal,from_price,to_price,order_by,category_id);
        call.enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
                MyFunctions.cancelLoading();
                ProductListResponse productResponse = response.body();
                if(productResponse.getSuccess()){

                    findViewById(R.id.order_empty).setVisibility(View.GONE);
                    product_recycler.setVisibility(View.VISIBLE);

                    productAdapter = new ProductsAdapter(productResponse.getData(),getApplicationContext());
                    product_recycler.setAdapter(productAdapter);
                }else {

                    findViewById(R.id.order_empty).setVisibility(View.VISIBLE);
                    product_recycler.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<ProductListResponse> call, Throwable t) {
                MyFunctions.cancelLoading();
                Toast.makeText(getApplicationContext(),Constants.API_ERROR , Toast.LENGTH_SHORT).show();

            }
        });
    }
}