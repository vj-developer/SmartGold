package com.greymatter.smartgold.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.greymatter.smartgold.R;
import com.greymatter.smartgold.adapter.OfferLockAdapter;
import com.greymatter.smartgold.adapter.OrderAdapter;
import com.greymatter.smartgold.model.CartListResponse;
import com.greymatter.smartgold.model.OfferLockResponse;
import com.greymatter.smartgold.model.OrderResponse;
import com.greymatter.smartgold.retrofit.APIInterface;
import com.greymatter.smartgold.retrofit.ApiConfig;
import com.greymatter.smartgold.retrofit.RetrofitBuilder;
import com.greymatter.smartgold.utils.Constants;
import com.greymatter.smartgold.utils.MyFunctions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderListActivity extends AppCompatActivity {

    RecyclerView order_recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        order_recycler = findViewById(R.id.order_recycler);

        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        getOrderList();
    }

    private void getOrderList() {
        MyFunctions.showLoading(OrderListActivity.this);
        String user_id = MyFunctions.getStringFromSharedPref(OrderListActivity.this, Constants.USERID,"");

        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);
        Call<OrderResponse> call = apiInterface.order_list(ApiConfig.SecurityKey,Constants.AccessKeyVal,user_id);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {

                MyFunctions.cancelLoading();
                if (response.isSuccessful()){

                    OrderResponse cartListResponse = response.body();
                    if(cartListResponse.getSuccess()){

                        findViewById(R.id.order_empty).setVisibility(View.GONE);
                        order_recycler.setVisibility(View.VISIBLE);

                        OrderAdapter orderAdapter = new OrderAdapter(cartListResponse.getData(),OrderListActivity.this);
                        order_recycler.setAdapter(orderAdapter);

                    }else {
                        findViewById(R.id.order_empty).setVisibility(View.VISIBLE);
                        order_recycler.setVisibility(View.GONE);
                    }

                }else {
                    Toast.makeText(OrderListActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                MyFunctions.cancelLoading();
                Toast.makeText(OrderListActivity.this, Constants.API_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }
}