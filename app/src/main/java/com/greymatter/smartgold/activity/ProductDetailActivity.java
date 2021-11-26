package com.greymatter.smartgold.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.greymatter.smartgold.R;
import com.greymatter.smartgold.utils.Constants;

import static android.content.ContentValues.TAG;

public class ProductDetailActivity extends AppCompatActivity {
    TextView pname,pprice,pdescription,product_title,discounted_price,total_price;
    ImageView imageView,backbtn;
    String namestr,pricestr,descstr,imagestr,discounted_price_str,product_id;
    ElegantNumberButton elegantNumberButton;
    int quantity=1,total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        namestr = getIntent().getStringExtra(Constants.PRODUCT_NAME);
        product_id = getIntent().getStringExtra(Constants.PRODUCT_ID);
        pricestr = getIntent().getStringExtra(Constants.PRICE);
        descstr = getIntent().getStringExtra(Constants.DESCRIPTION);
        imagestr = getIntent().getStringExtra(Constants.IMGURL);
        discounted_price_str = getIntent().getStringExtra(Constants.DISCOUNT_PRICE);

        pname = findViewById(R.id.product_name);
        product_title = findViewById(R.id.product_title);
        pprice = findViewById(R.id.prodct_price);
        discounted_price = findViewById(R.id.discounted_price);
        pdescription = findViewById(R.id.product_description);
        imageView = findViewById(R.id.imageView);
        backbtn = findViewById(R.id.backbtn);
        elegantNumberButton = findViewById(R.id.elegantNumberButton);
        total_price = findViewById(R.id.total_price);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        pname.setText(namestr);
        product_title.setText(namestr);
        pprice.setText(Constants.RUPEES+pricestr);
        discounted_price.setText(Constants.RUPEES+discounted_price_str);

        //striked text
        pprice.setPaintFlags(pprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        pdescription.setText(descstr);
        Glide.with(this)
                .load(imagestr)
                .fitCenter()
                .into(imageView);

        calculateTotal();
        elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Log.d(TAG, String.format("oldValue: %d   newValue: %d", oldValue, newValue));
                quantity = newValue;
                calculateTotal();
            }
        });

        elegantNumberButton.setOnClickListener(new ElegantNumberButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity = Integer.parseInt(elegantNumberButton.getNumber());
            }
        });

    }

    private void calculateTotal() {
        total = quantity * Integer.parseInt(discounted_price_str);
        total_price.setText("Total Price = ₹"+total);
    }
}