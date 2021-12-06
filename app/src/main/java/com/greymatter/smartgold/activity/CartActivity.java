package com.greymatter.smartgold.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.greymatter.smartgold.R;
import com.greymatter.smartgold.adapter.AddressAdapter;
import com.greymatter.smartgold.adapter.CartAdapter;
import com.greymatter.smartgold.model.AddToCartResponse;
import com.greymatter.smartgold.model.CartListResponse;
import com.greymatter.smartgold.retrofit.APIInterface;
import com.greymatter.smartgold.retrofit.RetrofitBuilder;
import com.greymatter.smartgold.utils.Constants;
import com.greymatter.smartgold.utils.MyFunctions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    CartAdapter cartAdapter;
    RecyclerView cart_recycler;
    private String Tag = "CartActivity";
    ImageView cart_empty;
    RelativeLayout cart_container;
    private List<CartListResponse.Datum> cartArrayList = new ArrayList<>();
    TextView total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        
        cart_recycler = findViewById(R.id.cart_recycler);
        cart_empty = findViewById(R.id.cart_empty);
        cart_container = findViewById(R.id.cart_container);
        total = findViewById(R.id.total);

        cartList();

        cartAdapter = new CartAdapter(cartArrayList, CartActivity.this, new CartAdapter.OnQuantityChangedListener() {
            @Override
            public void quantityChanged(String product_id, String quantity) {
                addToCart(product_id,quantity);
                Log.d(Tag,quantity);
            }

            @Override
            public void remove(String cart_id) {
                removeFromCart(cart_id);
            }
        });
        cart_recycler.setAdapter(cartAdapter);
        
        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartActivity.super.onBackPressed();
            }
        });

        findViewById(R.id.proceed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this,CheckoutActivity.class));
            }
        });

    }

    public void cartList() {
        MyFunctions.showLoading(CartActivity.this);
        String user_id = MyFunctions.getStringFromSharedPref(CartActivity.this, Constants.USERID,"");

        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);
        Call<CartListResponse> call = apiInterface.cart_list(user_id);
        call.enqueue(new Callback<CartListResponse>() {
            @Override
            public void onResponse(Call<CartListResponse> call, Response<CartListResponse> response) {
                MyFunctions.cancelLoading();
                if (response.isSuccessful()){
                    cartArrayList.clear();
                    CartListResponse cartListResponse = response.body();
                    if(cartListResponse.getSuccess()){

                        cart_empty.setVisibility(View.GONE);
                        cart_container.setVisibility(View.VISIBLE);

                        cartArrayList.addAll(cartListResponse.getData());
                        cartAdapter.notifyDataSetChanged();

                        calculateTotal();
                    }else {
                        cart_empty.setVisibility(View.VISIBLE);
                        cart_container.setVisibility(View.GONE);
                    }

                }else {
                    Toast.makeText(CartActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CartListResponse> call, Throwable t) {
                MyFunctions.cancelLoading();
                Toast.makeText(CartActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToCart(String product_id,String quantity) {
        MyFunctions.showLoading(CartActivity.this);
        String user_id = MyFunctions.getStringFromSharedPref(CartActivity.this,Constants.USERID,"");

        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);
        Call<AddToCartResponse> call = apiInterface.add_to_cart(user_id,product_id, String.valueOf(quantity));
        call.enqueue(new Callback<AddToCartResponse>() {
            @Override
            public void onResponse(Call<AddToCartResponse> call, Response<AddToCartResponse> response) {
                MyFunctions.cancelLoading();
                if (response.isSuccessful()){

                    AddToCartResponse addToCartResponse = response.body();
                    if(addToCartResponse.getSuccess()){
                        cartList();
                    }else {
                        Toast.makeText(CartActivity.this, addToCartResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(CartActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<AddToCartResponse> call, Throwable t) {
                MyFunctions.cancelLoading();
                Toast.makeText(CartActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void removeFromCart(String cart_id) {
        MyFunctions.showLoading(CartActivity.this);
        String user_id = MyFunctions.getStringFromSharedPref(CartActivity.this, Constants.USERID,"");

        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);
        Call<AddToCartResponse> call = apiInterface.remove_from_cart(user_id,cart_id);
        call.enqueue(new Callback<AddToCartResponse>() {
            @Override
            public void onResponse(Call<AddToCartResponse> call, Response<AddToCartResponse> response) {
                MyFunctions.cancelLoading();

                if (response.isSuccessful()){
                    AddToCartResponse addToCartResponse = response.body();
                    if(addToCartResponse.getSuccess()){
                        cartList();
                    }else {
                        Toast.makeText(CartActivity.this, addToCartResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(CartActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddToCartResponse> call, Throwable t) {
                MyFunctions.cancelLoading();
                Toast.makeText(CartActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void calculateTotal() {

        int total_amount=0;
        for (int i=0;i<cartArrayList.size();i++){
            total_amount += cartArrayList.get(i).getDiscountedPrice();
        }

        total.setText(Constants.RUPEES+total_amount);
    }

}