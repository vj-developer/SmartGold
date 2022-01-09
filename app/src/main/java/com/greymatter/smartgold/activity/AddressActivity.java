package com.greymatter.smartgold.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.greymatter.smartgold.R;
import com.greymatter.smartgold.adapter.AddressAdapter;
import com.greymatter.smartgold.model.AddressListResponse;
import com.greymatter.smartgold.retrofit.APIInterface;
import com.greymatter.smartgold.retrofit.ApiConfig;
import com.greymatter.smartgold.retrofit.RetrofitBuilder;
import com.greymatter.smartgold.utils.Constants;
import com.greymatter.smartgold.utils.MyFunctions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressActivity extends AppCompatActivity {
    AddressAdapter addressAdapter;
    RecyclerView address_recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        address_recycler = findViewById(R.id.address_recycleview);
        addressListApi();

        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private void addressListApi() {
        MyFunctions.showLoading(AddressActivity.this);
        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);

        Call<AddressListResponse> call = apiInterface.address_list(ApiConfig.SecurityKey,Constants.AccessKeyVal,MyFunctions.getStringFromSharedPref(AddressActivity.this,Constants.USERID,""));
        call.enqueue(new Callback<AddressListResponse>() {
            @Override
            public void onResponse(Call<AddressListResponse> call, Response<AddressListResponse> response) {
                MyFunctions.cancelLoading();
                AddressListResponse addressListResponse = response.body();
                if(addressListResponse.getSuccess()){
                   addressAdapter = new AddressAdapter(addressListResponse.getData(),AddressActivity.this);
                   address_recycler.setAdapter(addressAdapter);
                }
            }

            @Override
            public void onFailure(Call<AddressListResponse> call, Throwable t) {
                MyFunctions.cancelLoading();
                Toast.makeText(getApplicationContext(), Constants.API_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void openAddAddress(View view) {
        Intent intent = new Intent(AddressActivity.this, AddAddressActivity.class);
        startActivity(intent);
        finish();
    }
}