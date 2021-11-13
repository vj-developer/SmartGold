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
import com.greymatter.smartgold.retrofit.RetrofitBuilder;
import com.greymatter.smartgold.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReputedShopListActivity extends AppCompatActivity {

    SmartOffersAdapter smartOffersAdapter;
    RecyclerView smartoffers_recycler;
    String budget_id, latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reputed_shop_list);

        budget_id = getIntent().getStringExtra(Constants.BUDGET_ID);
        latitude = getIntent().getStringExtra(Constants.LATITUDE);
        longitude = getIntent().getStringExtra(Constants.LONGITUDE);


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
        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Call<SmartOffersResponse> call = apiInterface.smart_offers(budget_id,latitude,longitude);
        call.enqueue(new Callback<SmartOffersResponse>() {
            @Override
            public void onResponse(Call<SmartOffersResponse> call, Response<SmartOffersResponse> response) {
                progressDialog.dismiss();
                SmartOffersResponse smartOffersResponse = response.body();
                if(smartOffersResponse.getSuccess()){
                    smartOffersAdapter = new SmartOffersAdapter(smartOffersResponse.getData(),ReputedShopListActivity.this);
                    smartoffers_recycler.setAdapter(smartOffersAdapter);
                }
            }

            @Override
            public void onFailure(Call<SmartOffersResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext() ,t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}