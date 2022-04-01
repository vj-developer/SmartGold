package com.greymatter.smartgold.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;

import com.greymatter.smartgold.MainActivity;
import com.greymatter.smartgold.R;
import com.greymatter.smartgold.utils.Constants;
import com.greymatter.smartgold.utils.MyFunctions;

import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splash = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(splash);
                finish();
            }
        },3000);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            getGoldPrice();
        }
    }

    private void getGoldPrice() {
        String response = MyFunctions.getResponseFromUrl("https://gold--price.herokuapp.com/?format=json");
        JSONObject obj;
        try {
            obj = new JSONObject(response);
            String price = obj.getString("price");
            MyFunctions.saveStringToSharedPref(getApplicationContext(), Constants.GOLD_RATE,price);
        }catch (Exception e){e.printStackTrace();}
    }

}