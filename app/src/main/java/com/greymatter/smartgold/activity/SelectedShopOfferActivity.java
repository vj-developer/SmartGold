package com.greymatter.smartgold.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.greymatter.smartgold.R;
import com.greymatter.smartgold.adapter.SmartOffersAdapter;
import com.greymatter.smartgold.model.SmartOffersResponse;
import com.greymatter.smartgold.retrofit.APIInterface;
import com.greymatter.smartgold.retrofit.RetrofitBuilder;
import com.greymatter.smartgold.utils.Constants;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectedShopOfferActivity extends AppCompatActivity implements PaymentResultListener {

    String budget_id, latitude,longitude;
    TextView wastage,wastage_card_tv;
    Button lock_offer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_shop_offer);

        String initalAmount = "500";
        int amount = Math.round(Float.parseFloat(initalAmount) * 100);

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
                Checkout checkout = new Checkout();
                checkout.setKeyID(Constants.RAZORAPIKEY);
                JSONObject object = new JSONObject();
                try {
                    object.put("name","Smart Gold");
                    object.put("description","Lock your smart offer");
                    object.put("theme:colour","#F2CF8D");
                    object.put("currency","INR");
                    object.put("amount",amount);
                    object.put("prefill.contact","9876543210");
                    object.put("prefill.email","smartgold@gmail.com");
                    checkout.open(SelectedShopOfferActivity.this,object);
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onPaymentSuccess(String s) {
        Intent intent = new Intent(this,SmartGoldLockedActivity.class);
        intent.putExtra(Constants.PAYMENT,Constants.SUCCESS);
        startActivity(intent);
        finish();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,SmartGoldLockedActivity.class);
        intent.putExtra(Constants.PAYMENT,Constants.FAIL);
        startActivity(intent);
    }
}