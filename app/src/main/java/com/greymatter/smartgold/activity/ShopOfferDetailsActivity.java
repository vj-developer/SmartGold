package com.greymatter.smartgold.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.greymatter.smartgold.R;
import com.greymatter.smartgold.adapter.CategoryAdapter;
import com.greymatter.smartgold.model.CategoryResponse;
import com.greymatter.smartgold.model.OfferLockResponse;
import com.greymatter.smartgold.model.PriceDurationResponse;
import com.greymatter.smartgold.model.SmartOffersResponse;
import com.greymatter.smartgold.retrofit.APIInterface;
import com.greymatter.smartgold.retrofit.ApiConfig;
import com.greymatter.smartgold.retrofit.RetrofitBuilder;
import com.greymatter.smartgold.utils.Constants;
import com.greymatter.smartgold.utils.MyFunctions;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopOfferDetailsActivity extends AppCompatActivity implements PaymentResultListener {

    TextView wastage,wastage_card_tv,amount_tv,day_tv,claimed_tv,product_count,gram_price,details,price_card_tv;
    String amount_string,duration_day,shop_id,user_id,offer_id,gram_price_str,wastage_str;
    RecyclerView available_products;
    CategoryAdapter categoryAdapter;
    private SmartOffersResponse.Datum smartOffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_offer_details);

        wastage = findViewById(R.id.wastage);
        wastage_card_tv = findViewById(R.id.wastage_card_tv);
        price_card_tv = findViewById(R.id.price_card_tv);
        available_products = findViewById(R.id.available_products);
        amount_tv = findViewById(R.id.amount_tv);
        day_tv = findViewById(R.id.day_tv);
        claimed_tv = findViewById(R.id.claimed_tv);
        product_count = findViewById(R.id.product_count);
        gram_price = findViewById(R.id.gram_price);
        details = findViewById(R.id.details);

        shop_id = getIntent().getStringExtra(Constants.SELLER_ID);
        offer_id = getIntent().getStringExtra(Constants.OFFER_ID);
        gram_price_str = getIntent().getStringExtra(Constants.GRAMPRICE);
        wastage_str = getIntent().getStringExtra(Constants.WASTAGE);

        smartOffer = new Gson().fromJson(getIntent().getStringExtra(Constants.SMART_OFFER), SmartOffersResponse.Datum.class);
        getPriceDuration();
        //getAvailableProducts();


        wastage_card_tv.setText(wastage_str+ "% on wastage");
        price_card_tv.setText(MyFunctions.ConvertToINR(gram_price_str)+ " per gram");
        details.setText(smartOffer.getOfferDetails());

        /*Set points*/
        product_count.setText(". "+smartOffer.getTotal_products()+Constants.AVAILABLE_PRODUCT_COUNT);
        String gram_price_formated = ". Discount price per gram: "+ MyFunctions.ConvertToINR(gram_price_str);
        String wastage_formated = ". Discount on wastage: "+wastage_str + "%";

        gram_price.setText(gram_price_formated);
        wastage.setText(wastage_formated);

        gram_price.setVisibility(gram_price_str.equals("0") ? View.GONE:View.VISIBLE);
        wastage.setVisibility(wastage_str.equals("0") ? View.GONE:View.VISIBLE);

        claimed_tv.setText(Constants.THIS_OFFER_CAN_BE_CLAIMED_UNTIL + smartOffer.getValidDate());

        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.offer_lock_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MyFunctions.getBooleanFromSharedPref(ShopOfferDetailsActivity.this,Constants.ISLOGGEDIN,false)){
                    openRazorPay();
                }else {
                    Intent intent = new Intent(ShopOfferDetailsActivity.this, SigninActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void openRazorPay() {
        Checkout checkout = new Checkout();
        checkout.setKeyID(Constants.RAZORAPIKEY);
        JSONObject object = new JSONObject();

        int amount = Math.round(Float.parseFloat(amount_string) * 100);

        try {
            object.put("name","Smart Gold");
            object.put("description","Lock your smart offer");
            object.put("theme.color","#F2CF8D");
            object.put("currency","INR");
            object.put("amount",amount);
            object.put("prefill.contact",MyFunctions.getStringFromSharedPref(ShopOfferDetailsActivity.this,Constants.MOBILE," "));
            object.put("prefill.email",MyFunctions.getStringFromSharedPref(ShopOfferDetailsActivity.this,Constants.EMAIL," "));
            checkout.open(ShopOfferDetailsActivity.this,object);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void getAvailableProducts() {

        MyFunctions.showLoading(ShopOfferDetailsActivity.this);
        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);

        Call<CategoryResponse> call = apiInterface.available_category(ApiConfig.SecurityKey,Constants.AccessKeyVal,shop_id);
        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                MyFunctions.cancelLoading();
                CategoryResponse categoryResponse = response.body();
                if(categoryResponse.getSuccess()){
                    categoryAdapter = new CategoryAdapter(categoryResponse.getData(),ShopOfferDetailsActivity.this);
                    available_products.setAdapter(categoryAdapter);

                    product_count.setText(". "+categoryResponse.getData().size()+Constants.AVAILABLE_PRODUCT_COUNT);
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                MyFunctions.cancelLoading();
                Toast.makeText(getApplicationContext(), Constants.API_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPriceDuration() {
        MyFunctions.showLoading(ShopOfferDetailsActivity.this);
        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);
        Call<PriceDurationResponse> call = apiInterface.price_duration(ApiConfig.SecurityKey,Constants.AccessKeyVal);

        call.enqueue(new Callback<PriceDurationResponse>() {
            @Override
            public void onResponse(Call<PriceDurationResponse> call, Response<PriceDurationResponse> response) {
                MyFunctions.cancelLoading();
                PriceDurationResponse priceDurationResponse = response.body();

                if(priceDurationResponse.getSuccess()){


                    duration_day = priceDurationResponse.getData().getDays();
                    amount_string = priceDurationResponse.getData().getPrice();

                    amount_tv.setText(Constants.LOCK_PRICE_INSTRUCTION+amount_string);
                    if (Integer.parseInt(duration_day) > 1){
                        day_tv.setText(Constants.THIS_OFFER_CAN_BE_LOCKED_UNTIL +duration_day+" days");
                    }else {
                        day_tv.setText(Constants.THIS_OFFER_CAN_BE_LOCKED_UNTIL +duration_day+" day");
                    }

                }

            }

            @Override
            public void onFailure(Call<PriceDurationResponse> call, Throwable t) {
                MyFunctions.cancelLoading();
                Toast.makeText(getApplicationContext(),Constants.API_ERROR, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void lockOffer() {

        MyFunctions.showLoading(ShopOfferDetailsActivity.this);
        user_id = MyFunctions.getStringFromSharedPref(getApplicationContext(),Constants.USERID,"null");
        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);
        Call<OfferLockResponse> call = apiInterface.lock_offer(ApiConfig.SecurityKey,Constants.AccessKeyVal,user_id,shop_id,offer_id,amount_string);
        call.enqueue(new Callback<OfferLockResponse>() {
            @Override
            public void onResponse(Call<OfferLockResponse> call, Response<OfferLockResponse> response) {
                MyFunctions.cancelLoading();

                OfferLockResponse offerLockResponse = response.body();
                if (offerLockResponse.getSuccess()){
                    Intent intent = new Intent(getApplicationContext(), OfferLockResultActivity.class);
                    intent.putExtra(Constants.PAYMENT,Constants.SUCCESS);
                    intent.putExtra(Constants.REF_ID,offerLockResponse.getData().getId());
                    intent.putExtra(Constants.STORE_NAME,offerLockResponse.getData().getStoreName());

                    String address = offerLockResponse.getData().getStreet()+
                            ", "+offerLockResponse.getData().getCity()+
                            " - "+offerLockResponse.getData().getPincode();

                    intent.putExtra(Constants.STORE_ADDRESS,address);
                    intent.putExtra(Constants.VALID_TILL,offerLockResponse.getData().getValidTill());
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onFailure(Call<OfferLockResponse> call, Throwable t) {
                MyFunctions.cancelLoading();
                Toast.makeText(getApplicationContext(), Constants.API_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onPaymentSuccess(String s) {
        lockOffer();
    }


    @Override
    public void onPaymentError(int i, String s) {
        //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, OfferLockResultActivity.class);
        intent.putExtra(Constants.PAYMENT,Constants.FAIL);
        startActivity(intent);
    }
}