package com.greymatter.smartgold.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.greymatter.smartgold.R;
import com.greymatter.smartgold.adapter.ProductsAdapter;
import com.greymatter.smartgold.model.ProductListResponse;
import com.greymatter.smartgold.retrofit.APIInterface;
import com.greymatter.smartgold.retrofit.RetrofitBuilder;
import com.greymatter.smartgold.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListActivity extends AppCompatActivity {
    RecyclerView product_recycler;
    ProductsAdapter productAdapter;
    String category_id,category_name;
    TextView CategoryTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        CategoryTitle = findViewById(R.id.category_title);
        product_recycler = findViewById(R.id.recyclerviewproduct);
        category_id = getIntent().getStringExtra(Constants.CATEGORY_ID);
        category_name = getIntent().getStringExtra(Constants.CATEGORY_NAME);
        CategoryTitle.setText(category_name);
        ProductList();

        findViewById(R.id.backbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void ProductList() {
        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);
        Call<ProductListResponse> call = apiInterface.category_products(category_id);
        call.enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
                ProductListResponse productResponse = response.body();
                if(productResponse.getSuccess()){
                    productAdapter = new ProductsAdapter(productResponse.getData(),ProductListActivity.this);
                    product_recycler.setAdapter(productAdapter);
                }

            }

            @Override
            public void onFailure(Call<ProductListResponse> call, Throwable t) {
                Toast.makeText(ProductListActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}