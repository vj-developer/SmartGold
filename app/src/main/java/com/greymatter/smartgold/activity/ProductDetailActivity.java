package com.greymatter.smartgold.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.greymatter.smartgold.R;
import com.greymatter.smartgold.model.AddAddressResponse;
import com.greymatter.smartgold.model.AddToCartResponse;
import com.greymatter.smartgold.model.ProductResponse;
import com.greymatter.smartgold.retrofit.APIInterface;
import com.greymatter.smartgold.retrofit.ApiConfig;
import com.greymatter.smartgold.retrofit.RetrofitBuilder;
import com.greymatter.smartgold.utils.Constants;
import com.greymatter.smartgold.utils.MyFunctions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ProductDetailActivity extends AppCompatActivity {

    TextView pname,pprice,pdescription,product_title,discounted_price,total_price,shop_name_tv,cart_count,gender,gram;
    ImageView imageView,backbtn;
    String namestr,pricestr,descstr,imagestr,discounted_price_str,product_id,shop_name;
    ElegantNumberButton elegantNumberButton;
    int quantity=1,total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        product_id = getIntent().getStringExtra(Constants.PRODUCT_ID);

        pname = findViewById(R.id.product_name);
        product_title = findViewById(R.id.product_title);
        pprice = findViewById(R.id.prodct_price);
        discounted_price = findViewById(R.id.discounted_price);
        pdescription = findViewById(R.id.product_description);
        imageView = findViewById(R.id.imageView);
        backbtn = findViewById(R.id.backbtn);
        elegantNumberButton = findViewById(R.id.elegantNumberButton);
        total_price = findViewById(R.id.total_price);
        shop_name_tv = findViewById(R.id.shop_name_tv);
        cart_count = findViewById(R.id.cart_count);
        gender = findViewById(R.id.gender);
        gram = findViewById(R.id.gram);




        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getProductDetails();

        //striked text
        pprice.setPaintFlags(pprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

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

        findViewById(R.id.add_to_cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyFunctions.getBooleanFromSharedPref(getApplicationContext(),Constants.ISLOGGEDIN,false)){
                    addToCart();
                }else {
                    startActivity(new Intent(getApplicationContext(),SigninActivity.class));
                }
            }
        });

        findViewById(R.id.cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CartActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*Cart count*/
        int count = Integer.parseInt(MyFunctions.getStringFromSharedPref(getApplicationContext(),Constants.CART_COUNT,"0"));
        if (count > 0 ) cart_count.setText(count+"");
        else cart_count.setVisibility(View.GONE);
    }

    private void getProductDetails() {
        MyFunctions.showLoading(ProductDetailActivity.this);

        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);
        Call<ProductResponse> call = apiInterface.getProductDetails(ApiConfig.SecurityKey,Constants.AccessKeyVal,product_id);
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                MyFunctions.cancelLoading();

                if (response.isSuccessful()){

                    ProductResponse body = response.body();
                    if(body.getSuccess()){
                        namestr = body.getData().get(0).getName();
                        pricestr = body.getData().get(0).getPrice();
                        descstr = body.getData().get(0).getDescription();
                        imagestr = body.getData().get(0).getImage();
                        discounted_price_str = body.getData().get(0).getDiscountedPrice();
                        shop_name = body.getData().get(0).getStoreName();

                        updateView(body);
                    }else {
                        Toast.makeText(getApplicationContext(), Constants.API_ERROR , Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                MyFunctions.cancelLoading();
                Toast.makeText(getApplicationContext(), Constants.API_ERROR , Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateView(ProductResponse body) {

        pname.setText(namestr);
        product_title.setText(namestr);
        pprice.setText(MyFunctions.ConvertToINR(pricestr));
        discounted_price.setText(MyFunctions.ConvertToINR(discounted_price_str));
        shop_name_tv.setText(shop_name);

        pdescription.setText(descstr);
        Glide.with(this)
                .load(imagestr)
                .fitCenter()
                .into(imageView);

        gender.setText(body.getData().get(0).getGender());
        gram.setText("Gross weight "+body.getData().get(0).getWeight()+"gm");

        calculateTotal();

        if (body.getData().get(0).getName().toLowerCase().contains("bangle") || body.getData().get(0).getName().toLowerCase().contains("ring")){

        }

    }

    private void addToCart() {
        MyFunctions.showLoading(ProductDetailActivity.this);
        String user_id = MyFunctions.getStringFromSharedPref(ProductDetailActivity.this,Constants.USERID,"");

        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);
        Call<AddToCartResponse> call = apiInterface.add_to_cart(ApiConfig.SecurityKey,Constants.AccessKeyVal,user_id,product_id, String.valueOf(quantity));
        call.enqueue(new Callback<AddToCartResponse>() {
            @Override
            public void onResponse(Call<AddToCartResponse> call, Response<AddToCartResponse> response) {
                MyFunctions.cancelLoading();

                if (response.isSuccessful()){

                    AddToCartResponse addToCartResponse = response.body();
                    Toast.makeText(getApplicationContext(), addToCartResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    if(addToCartResponse.getSuccess()){
                        startActivity(new Intent(getApplicationContext(),CartActivity.class));
                    }

                }else {
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<AddToCartResponse> call, Throwable t) {
                MyFunctions.cancelLoading();
                Toast.makeText(getApplicationContext(), Constants.API_ERROR , Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void calculateTotal() {
        total = quantity * Integer.parseInt(discounted_price_str);
        total_price.setText(MyFunctions.ConvertToINR(String.valueOf(total)));
    }
}