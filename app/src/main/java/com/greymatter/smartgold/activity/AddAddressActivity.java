package com.greymatter.smartgold.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.greymatter.smartgold.R;
import com.greymatter.smartgold.model.AddAddressResponse;
import com.greymatter.smartgold.retrofit.APIInterface;
import com.greymatter.smartgold.retrofit.RetrofitBuilder;
import com.greymatter.smartgold.utils.Constants;
import com.greymatter.smartgold.utils.MyFunctions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAddressActivity extends AppCompatActivity {

    EditText user_name_et,address_et, address_optional_et, area_et, city_et, pin_code_et;
    Button add_address;
    String user_name, address, address_optional , area, city, pin_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        user_name_et = findViewById(R.id.name);
        address_et = findViewById(R.id.address);
        address_optional_et = findViewById(R.id.address_optional);
        area_et = findViewById(R.id.area);
        city_et = findViewById(R.id.city);
        pin_code_et = findViewById(R.id.pincode);
        add_address = findViewById(R.id.add_address_btn);

        add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_name = user_name_et.getText().toString().trim();
                address = address_et.getText().toString().trim();
                address_optional = address_optional_et.getText().toString().trim();
                area = area_et.getText().toString().trim();
                city = city_et.getText().toString().trim();
                pin_code = pin_code_et.getText().toString().trim();

                if(isValid()){
                    AddressApiCall();
                }
            }
        });
        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void AddressApiCall() {
        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.show();
        String user_id = MyFunctions.getStringFromSharedPref(AddAddressActivity.this,Constants.USERID,"");

        Call<AddAddressResponse> call = apiInterface.add_address(user_id,user_name,address,address_optional,city,area,pin_code);
        call.enqueue(new Callback<AddAddressResponse>() {
            @Override
            public void onResponse(Call<AddAddressResponse> call, Response<AddAddressResponse> response) {
                progressDialog.dismiss();
                AddAddressResponse addAddressResponse = response.body();
                Toast.makeText(getApplicationContext(), addAddressResponse.getMessage(), Toast.LENGTH_SHORT).show();
                if(addAddressResponse.getSuccess()){
                    Intent intent = new Intent(AddAddressActivity.this, AddressActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<AddAddressResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private boolean isValid() {
        if(user_name.isEmpty()){
            user_name_et.setError("Invalid name");
        }
        if(address.isEmpty()){
            address_et.setError("Invalid address");
            return false;
        }
        if(area.isEmpty()){
            area_et.setError("Invalid area");
            return false;
        }
        if(city.isEmpty()){
            city_et.setError("Invalid city");
            return false;
        }
        if(pin_code.length() !=6){
            pin_code_et.setError("Invalid pincode");
            return false;
        }
        return true;
    }
}