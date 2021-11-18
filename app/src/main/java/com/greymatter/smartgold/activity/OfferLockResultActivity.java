package com.greymatter.smartgold.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.greymatter.smartgold.MainActivity;
import com.greymatter.smartgold.R;
import com.greymatter.smartgold.utils.Constants;

public class OfferLockResultActivity extends AppCompatActivity {

    TextView ref_id_tv,shop_name_tv,shop_location_tv,shop_validity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_gold_locked);

        ref_id_tv = findViewById(R.id.ref_id_tv);
        shop_name_tv = findViewById(R.id.shop_name_tv);
        shop_location_tv = findViewById(R.id.shop_location_tv);
        shop_validity = findViewById(R.id.shop_validity);

        String payment_details = getIntent().getStringExtra(Constants.PAYMENT);


        if(payment_details.equals(Constants.SUCCESS)){
            findViewById(R.id.payment_fail).setVisibility(View.GONE);

            ref_id_tv.setText("Ref id: "+getIntent().getStringExtra(Constants.REF_ID));
            shop_name_tv.setText("Shop name: "+getIntent().getStringExtra(Constants.STORE_NAME));
            shop_location_tv.setText("Shop address: "+getIntent().getStringExtra(Constants.STORE_ADDRESS));
            shop_validity.setText("Valid Till:  "+getIntent().getStringExtra(Constants.VALID_TILL));
        }
        else{
            findViewById(R.id.payment_success).setVisibility(View.GONE);
        }

        findViewById(R.id.continue_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }
}