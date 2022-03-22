package com.greymatter.smartgold.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.greymatter.smartgold.R;
import com.greymatter.smartgold.adapter.SmartOffersAdapter;
import com.greymatter.smartgold.model.SmartOffersResponse;
import com.greymatter.smartgold.retrofit.APIInterface;
import com.greymatter.smartgold.retrofit.ApiConfig;
import com.greymatter.smartgold.retrofit.RetrofitBuilder;
import com.greymatter.smartgold.utils.Constants;
import com.greymatter.smartgold.utils.MyFunctions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopListActivity extends AppCompatActivity {

    SmartOffersAdapter smartOffersAdapter;
    RecyclerView smartoffers_recycler;
    String budget_id, latitude,longitude,range_to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);

        budget_id = getIntent().getStringExtra(Constants.BUDGET_ID);
        latitude = getIntent().getStringExtra(Constants.LATITUDE);
        longitude = getIntent().getStringExtra(Constants.LONGITUDE);
        range_to = getIntent().getStringExtra(Constants.RANGE_TO);


        smartoffers_recycler = findViewById(R.id.shop_recycleview);
        SmartOffersApi();

        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void SmartOffersApi() {

        MyFunctions.showLoading(ShopListActivity.this);
        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);
        Call<SmartOffersResponse> call = apiInterface.smart_offers(ApiConfig.SecurityKey,Constants.AccessKeyVal,budget_id,latitude,longitude,range_to);
        call.enqueue(new Callback<SmartOffersResponse>() {
            @Override
            public void onResponse(Call<SmartOffersResponse> call, Response<SmartOffersResponse> response) {
                MyFunctions.cancelLoading();

                SmartOffersResponse smartOffersResponse = response.body();
                if(smartOffersResponse.getSuccess()){

                    findViewById(R.id.order_empty).setVisibility(View.GONE);
                    findViewById(R.id.container).setVisibility(View.VISIBLE);

                    smartOffersAdapter = new SmartOffersAdapter(smartOffersResponse.getData(), ShopListActivity.this);
                    smartoffers_recycler.setAdapter(smartOffersAdapter);
                }else {

                    findViewById(R.id.order_empty).setVisibility(View.VISIBLE);
                    findViewById(R.id.container).setVisibility(View.GONE);
                    //Toast.makeText(ShopListActivity.this, smartOffersResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SmartOffersResponse> call, Throwable t) {
                MyFunctions.cancelLoading();
                Toast.makeText(getApplicationContext() ,Constants.API_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }
}