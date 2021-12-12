package com.greymatter.smartgold.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.greymatter.smartgold.MainActivity;
import com.greymatter.smartgold.R;
import com.greymatter.smartgold.utils.Constants;

public class OrderResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_result);

        String payment_details = getIntent().getStringExtra(Constants.STATUS);

        if(payment_details.equals(Constants.SUCCESS)){
            findViewById(R.id.payment_fail).setVisibility(View.GONE);

        }
        else{
            findViewById(R.id.payment_success).setVisibility(View.GONE);
        }

        findViewById(R.id.continue_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payment_details.equals(Constants.SUCCESS)){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }else {
                    finish();
                }
            }
        });
    }
}