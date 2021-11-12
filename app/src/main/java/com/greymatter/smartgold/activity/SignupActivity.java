package com.greymatter.smartgold.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.greymatter.smartgold.R;
import com.greymatter.smartgold.model.LoginResponse;
import com.greymatter.smartgold.retrofit.APIInterface;
import com.greymatter.smartgold.retrofit.RetrofitBuilder;
import com.greymatter.smartgold.utils.Constants;
import com.greymatter.smartgold.utils.MyFunctions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    Button signup;
    TextView signinLink;
    EditText fullName_et, mobileNumber_et,emailId_et;
    String fullName, mobileNumber,emailId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signup = findViewById(R.id.signup);
        signinLink = findViewById(R.id.signinlink);
        fullName_et = findViewById(R.id.fulname);
        mobileNumber_et = findViewById(R.id.number);
        emailId_et = findViewById(R.id.email);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullName = fullName_et.getText().toString().trim();
                mobileNumber = mobileNumber_et.getText().toString().trim();
                emailId = emailId_et.getText().toString().trim();

                if(isValid()){
                    loginApiCall(mobileNumber);
                }
            }
        });
        signinLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginApiCall(String mobileNumber) {
        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.show();

        Call<LoginResponse> call = apiInterface.login(mobileNumber);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressDialog.dismiss();
                LoginResponse loginResponse = response.body();
                if(loginResponse.getSuccess()){
                    Toast.makeText(getApplicationContext(),loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
                else{
                    MyFunctions.saveStringToSharedPref(SignupActivity.this, Constants.NAME, fullName);
                    MyFunctions.saveStringToSharedPref(SignupActivity.this,Constants.EMAIL, emailId);
                    MyFunctions.saveStringToSharedPref(SignupActivity.this, Constants.MOBILE, mobileNumber);
                    OTPActivity();
                }
            }


            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        }

    private boolean isValid() {
        if (fullName.isEmpty()){
            fullName_et.setError("Enter the full name");
            return false;
        }
        if(emailId.isEmpty()){
            emailId_et.setError("Invalid Emailid");
            return false;
        }
        if (mobileNumber.length() != 10){
            mobileNumber_et.setError("Invalid mobile number");
            return false;
        }
        return true;
    }

    private void OTPActivity() {
        Intent intent = new Intent(SignupActivity.this, OtpActivity.class);
        intent.putExtra("type","register");
        startActivity(intent);
        finish();
    }
}