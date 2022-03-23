package com.greymatter.smartgold.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.greymatter.smartgold.R;
import com.greymatter.smartgold.adapter.CategoryAdapter;
import com.greymatter.smartgold.model.CategoryResponse;
import com.greymatter.smartgold.model.StoreResponse;
import com.greymatter.smartgold.retrofit.APIInterface;
import com.greymatter.smartgold.retrofit.ApiConfig;
import com.greymatter.smartgold.retrofit.RetrofitBuilder;
import com.greymatter.smartgold.utils.Constants;
import com.greymatter.smartgold.utils.MyFunctions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryListActivity extends AppCompatActivity {

    String store_id;
    TextView store_name;
    StoreResponse.Datum storeResponse;
    CategoryAdapter categoryAdapter;
    RecyclerView category_recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);

        store_name = findViewById(R.id.store_name);
        category_recycler = findViewById(R.id.category_recycler);

        storeResponse = new Gson().fromJson(getIntent().getStringExtra(Constants.STORE),StoreResponse.Datum.class);

        store_id = storeResponse.getId();
        store_name.setText(storeResponse.getStoreName());

        getAvailableCategory();

        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void getAvailableCategory() {

        MyFunctions.showLoading(CategoryListActivity.this);
        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);

        Call<CategoryResponse> call = apiInterface.available_category(ApiConfig.SecurityKey,Constants.AccessKeyVal,store_id);
        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                MyFunctions.cancelLoading();
                CategoryResponse categoryResponse = response.body();
                if(categoryResponse.getSuccess()){
                    categoryAdapter = new CategoryAdapter(categoryResponse.getData(),CategoryListActivity.this);
                    category_recycler.setAdapter(categoryAdapter);
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                MyFunctions.cancelLoading();
                Toast.makeText(getApplicationContext(), Constants.API_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

}