package com.greymatter.smartgold.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.greymatter.smartgold.R;
import com.greymatter.smartgold.model.AddToCartResponse;
import com.greymatter.smartgold.model.CheckoutResponse;
import com.greymatter.smartgold.model.DefaultAddressResponse;
import com.greymatter.smartgold.retrofit.APIInterface;
import com.greymatter.smartgold.retrofit.RetrofitBuilder;
import com.greymatter.smartgold.utils.Constants;
import com.greymatter.smartgold.utils.MyFunctions;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity implements PaymentResultListener {

    private static final String TAG = "Checkout";
    RadioButton pickup_at_store,delivery_at_home;
    TextView product_count,original_price,discount,delivery_charge,order_total,address_name,address_tv,pincode;
    private String method = "1",amount_string; /*Default Pick up at the store*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        pickup_at_store = findViewById(R.id.pickup_at_store);
        delivery_at_home = findViewById(R.id.delivery_at_home);
        product_count = findViewById(R.id.product_count);
        original_price = findViewById(R.id.original_price);
        discount = findViewById(R.id.discount);
        delivery_charge = findViewById(R.id.delivery_charge);
        order_total = findViewById(R.id.order_total);
        address_name = findViewById(R.id.address_name);
        address_tv = findViewById(R.id.address_tv);
        pincode = findViewById(R.id.pincode);

        getDefaultAddress();

        checkout();

        pickup_at_store.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    method = "1";
                    checkout();
                }
                else {
                    method ="2";
                    checkout();
                }
            }
        });

        findViewById(R.id.change_address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AddressActivity.class));
            }
        });
        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckoutActivity.super.onBackPressed();
            }
        });

        findViewById(R.id.place_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =new AlertDialog.Builder(CheckoutActivity.this);
                builder.setTitle("Are you sure?");
                builder.setMessage("To Place this order");
                builder.setPositiveButton("Pay Now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openRazorPay();
                    }
                });
                builder.setNegativeButton("Pay Later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        placeOrder(false);
                    }
                });
                builder.create().show();
                
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
            object.put("theme:colour","#F2CF8D");
            object.put("currency","INR");
            object.put("amount",amount);
            object.put("prefill.contact",MyFunctions.getStringFromSharedPref(CheckoutActivity.this,Constants.MOBILE," "));
            object.put("prefill.email",MyFunctions.getStringFromSharedPref(CheckoutActivity.this,Constants.EMAIL," "));
            checkout.open(CheckoutActivity.this,object);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void placeOrder(boolean isPaid) {
        MyFunctions.showLoading(CheckoutActivity.this);
        String user_id = MyFunctions.getStringFromSharedPref(CheckoutActivity.this,Constants.USERID,"");

        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);
        Call<CheckoutResponse> call = apiInterface.place_order(user_id,method,isPaid);
        call.enqueue(new Callback<CheckoutResponse>() {
            @Override
            public void onResponse(Call<CheckoutResponse> call, Response<CheckoutResponse> response) {
                MyFunctions.cancelLoading();
                if (response.isSuccessful()){
                    CheckoutResponse checkoutResponse = response.body();
                    if(checkoutResponse.getSuccess()){
                        startActivity(new Intent(getApplicationContext(),OrderResultActivity.class).putExtra(Constants.STATUS,Constants.SUCCESS));
                        finish();
                    }
                    Toast.makeText(CheckoutActivity.this, checkoutResponse.getMessage(), Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(CheckoutActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CheckoutResponse> call, Throwable t) {
                MyFunctions.cancelLoading();
                Toast.makeText(CheckoutActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDefaultAddress() {
        String user_id = MyFunctions.getStringFromSharedPref(CheckoutActivity.this,Constants.USERID,"");

        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);
        Call<DefaultAddressResponse> call = apiInterface.get_default_address(user_id);
        call.enqueue(new Callback<DefaultAddressResponse>() {
            @Override
            public void onResponse(Call<DefaultAddressResponse> call, Response<DefaultAddressResponse> response) {
                if (response.isSuccessful()){
                    DefaultAddressResponse defaultAddressResponse = response.body();
                    if(defaultAddressResponse.getSuccess()){
                        updateAddress(defaultAddressResponse.getData().get(0));
                    }else {
                        startActivity(new Intent(getApplicationContext(),AddAddressActivity.class));
                    }

                }else {
                    Toast.makeText(CheckoutActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultAddressResponse> call, Throwable t) {
                Toast.makeText(CheckoutActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateAddress(DefaultAddressResponse.Datum address) {
        String address_details = address.getAddress()+" , "+ address.getArea()+ " , ";
        String pincode_city = address.getCity()+" - "+address.getPincode();
        address_name.setText(address.getName());
        address_tv.setText(address_details);
        pincode.setText(pincode_city);
    }

    private void checkout() {
        MyFunctions.showLoading(CheckoutActivity.this);
        String user_id = MyFunctions.getStringFromSharedPref(CheckoutActivity.this,Constants.USERID,"");

        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);
        Call<CheckoutResponse> call = apiInterface.check_out(user_id,method);
        call.enqueue(new Callback<CheckoutResponse>() {
            @Override
            public void onResponse(Call<CheckoutResponse> call, Response<CheckoutResponse> response) {
                MyFunctions.cancelLoading();
                if (response.isSuccessful()){
                    CheckoutResponse checkoutResponse = response.body();
                    if(checkoutResponse.getSuccess()){
                        updateValues(checkoutResponse.getData());
                    }else {
                        Toast.makeText(CheckoutActivity.this, checkoutResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(CheckoutActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CheckoutResponse> call, Throwable t) {
                MyFunctions.cancelLoading();
                Toast.makeText(CheckoutActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateValues(CheckoutResponse.Data data) {
        product_count.setText(""+data.getNoOfProducts());
        original_price.setText(MyFunctions.ConvertToINR(String.valueOf(data.getTotalprice())));
        discount.setText(MyFunctions.ConvertToINR(String.valueOf(data.getSaved())));
        delivery_charge.setText(MyFunctions.ConvertToINR(String.valueOf(data.getDeliveryPrice())));
        order_total.setText(MyFunctions.ConvertToINR(String.valueOf(data.getGrandtotal())));
        amount_string = String.valueOf(data.getGrandtotal());
    }

    @Override
    protected void onResume() {
        super.onResume();

        getDefaultAddress();
    }

    @Override
    public void onPaymentSuccess(String s) {
        placeOrder(true);
    }

    @Override
    public void onPaymentError(int i, String s) {
        //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, OfferLockResultActivity.class);
        intent.putExtra(Constants.PAYMENT,Constants.FAIL);
        startActivity(intent);
    }
}