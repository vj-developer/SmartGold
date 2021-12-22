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

import com.greymatter.smartgold.MainActivity;
import com.greymatter.smartgold.R;
import com.greymatter.smartgold.model.LoginResponse;
import com.greymatter.smartgold.retrofit.APIInterface;
import com.greymatter.smartgold.retrofit.RetrofitBuilder;
import com.greymatter.smartgold.utils.Constants;
import com.greymatter.smartgold.utils.MyFunctions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SigninActivity extends AppCompatActivity {
    Button signin;
    TextView signupLink;
    EditText mobile_number_et;
    String mobile_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        signin = findViewById(R.id.signin);
        mobile_number_et = findViewById(R.id.number);
        signupLink = findViewById(R.id.signuplink);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile_number = mobile_number_et.getText().toString().trim();
                if (isValid()){
                    loginApiCall(mobile_number);
                }
            }
        });
        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SigninActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginApiCall(String mobile_number) {
        MyFunctions.showLoading(SigninActivity.this);
        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);
        Call<LoginResponse> call = apiInterface.login(mobile_number);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                MyFunctions.cancelLoading();
                LoginResponse loginResponse = response.body();
                if(loginResponse.getSuccess()){
                    MyFunctions.saveStringToSharedPref(SigninActivity.this, Constants.USERID,loginResponse.getData().get(0).getId());
                    MyFunctions.saveStringToSharedPref(SigninActivity.this, Constants.NAME,loginResponse.getData().get(0).getName());
                    MyFunctions.saveStringToSharedPref(SigninActivity.this, Constants.EMAIL,loginResponse.getData().get(0).getEmail());
                    MyFunctions.saveStringToSharedPref(SigninActivity.this, Constants.MOBILE,loginResponse.getData().get(0).getMobile());
                    openOTPactivity();
                }
                else{
                    Toast.makeText(getApplicationContext(), loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                MyFunctions.cancelLoading();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValid() {
        if(mobile_number.length() != 10){
            mobile_number_et.setError("Invalid Mobile number");
            return false;
        }

        return  true;
    }

    private void openOTPactivity() {
        Intent intent = new Intent(SigninActivity.this, OtpActivity.class);
        intent.putExtra("type","login");
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}