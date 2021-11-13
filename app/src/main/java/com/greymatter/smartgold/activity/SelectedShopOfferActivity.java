package com.greymatter.smartgold.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
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

public class SelectedShopOfferActivity extends AppCompatActivity {

    String budget_id, latitude,longitude;
    TextView wastage,wastage_card_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_shop_offer);

        budget_id = getIntent().getStringExtra(Constants.BUDGET_ID);
        latitude = getIntent().getStringExtra(Constants.LATITUDE);
        longitude = getIntent().getStringExtra(Constants.LONGITUDE);

        wastage = findViewById(R.id.wastage);
        wastage_card_tv = findViewById(R.id.wastage_card_tv);

        wastage.setText("."+getIntent().getStringExtra(Constants.WASTAGE)+ "% wastage");
        wastage_card_tv.setText(getIntent().getStringExtra(Constants.WASTAGE)+ "% wastage");

        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.offer_lock_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SmartGoldLockedActivity.class));
            }
        });


    }
}