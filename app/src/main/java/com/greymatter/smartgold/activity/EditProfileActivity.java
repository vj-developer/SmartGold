package com.greymatter.smartgold.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.greymatter.smartgold.R;
import com.greymatter.smartgold.model.CartListResponse;
import com.greymatter.smartgold.model.RegisterResponse;
import com.greymatter.smartgold.retrofit.APIInterface;
import com.greymatter.smartgold.retrofit.ApiConfig;
import com.greymatter.smartgold.retrofit.RetrofitBuilder;
import com.greymatter.smartgold.utils.Constants;
import com.greymatter.smartgold.utils.MyFunctions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    EditText fullName_et,emailId_et;
    String fullName,emailId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        fullName_et = findViewById(R.id.fulname);
        emailId_et = findViewById(R.id.email);

        fullName_et.setText(MyFunctions.getStringFromSharedPref(getApplicationContext(), Constants.NAME,""));
        emailId_et.setText(MyFunctions.getStringFromSharedPref(getApplicationContext(), Constants.EMAIL,""));

        findViewById(R.id.backbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullName = fullName_et.getText().toString().trim();
                emailId = emailId_et.getText().toString().trim();

                if(isValid()){
                    updateProfile();
                }
            }
        });

    }

    private void updateProfile() {
        MyFunctions.showLoading(EditProfileActivity.this);
        String user_id = MyFunctions.getStringFromSharedPref(EditProfileActivity.this, Constants.USERID,"");

        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);
        Call<RegisterResponse> call = apiInterface.updateProfile(ApiConfig.SecurityKey,Constants.AccessKeyVal,user_id,fullName,emailId);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                MyFunctions.cancelLoading();
                if (response.isSuccessful()){

                    RegisterResponse registerResponse = response.body();
                    Toast.makeText(getApplicationContext(),registerResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    if(registerResponse.getSuccess()){
                        MyFunctions.saveStringToSharedPref(getApplicationContext(), Constants.USERID,registerResponse.getData().get(0).getId());
                        MyFunctions.saveStringToSharedPref(getApplicationContext(), Constants.NAME,registerResponse.getData().get(0).getName());
                        MyFunctions.saveStringToSharedPref(getApplicationContext(), Constants.EMAIL,registerResponse.getData().get(0).getEmail());
                        MyFunctions.saveStringToSharedPref(getApplicationContext(), Constants.MOBILE,registerResponse.getData().get(0).getMobile());

                        finish();
                    }

                }else {
                    Toast.makeText(EditProfileActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                MyFunctions.cancelLoading();
                Toast.makeText(EditProfileActivity.this,Constants.API_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValid() {
        if (fullName.isEmpty()){
            fullName_et.setError("Enter the full name");
            return false;
        }
        if(emailId.isEmpty()){
            emailId_et.setError("Invalid Email Id");
            return false;
        }
        return true;
    }

}