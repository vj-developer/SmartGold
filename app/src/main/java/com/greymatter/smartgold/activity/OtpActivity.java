package com.greymatter.smartgold.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.greymatter.smartgold.MainActivity;
import com.greymatter.smartgold.R;
import com.greymatter.smartgold.model.RegisterResponse;
import com.greymatter.smartgold.retrofit.APIInterface;
import com.greymatter.smartgold.retrofit.RetrofitBuilder;
import com.greymatter.smartgold.utils.Constants;
import com.greymatter.smartgold.utils.MyFunctions;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpActivity extends AppCompatActivity {

    private static final String TAG = "OTP Activity";
    String type, otp, mobilenumber;
    Button submit;
    EditText otp_et;
    TextView resend_otp;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String mVerificationId;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        mAuth = FirebaseAuth.getInstance();
        type = getIntent().getStringExtra("type");
        submit = findViewById(R.id.otp_submit);
        otp_et = findViewById(R.id.otpet);
        resend_otp = findViewById(R.id.resend_otp);

        mobilenumber = MyFunctions.getStringFromSharedPref(OtpActivity.this, Constants.MOBILE,"");
        sendOTP(mobilenumber);

        resendTimer();

        resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(resend_otp.getText().toString().equals("Resend OTP")){
                    resendOTP(mobilenumber);
                    resendTimer();
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp = otp_et.getText().toString().trim();
                if(isValid()){
                    //verfiy OTP
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }

    private void resendOTP(String mobilenumber) {
            PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber("+91" + mobilenumber)       // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(this)                 // Activity (for callback binding)
                            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                            .setForceResendingToken(mResendToken)     // ForceResendingToken from callbacks
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void resendTimer() {
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                resend_otp.setText("Resend OTP in " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                resend_otp.setText("Resend OTP");
            }

        }.start();
    }

    private void sendOTP(String mobilenumber) {

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {

                        Log.d(TAG, "onVerificationCompleted:" + credential);
                        signInWithPhoneAuthCredential(credential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Log.w(TAG, "onVerificationFailed", e);

                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            // Invalid request
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            // The SMS quota for the project has been exceeded
                        }
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId,
                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {

                        Log.d(TAG, "onCodeSent:" + verificationId);

                        // Save verification ID and resending token so we can use them later
                        mVerificationId = verificationId;
                        mResendToken = token;
                    }
                };


        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91" + mobilenumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                            if(type.equals("login")){
                                openMainActivity();
                            }
                            else {
                                registerApicall();
                            }
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    private void openMainActivity() {
        MyFunctions.saveBooleanToSharedPref(OtpActivity.this,Constants.ISLOGGEDIN, true);
        Intent intent = new Intent(OtpActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void registerApicall() {
        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);
        MyFunctions.showLoading(OtpActivity.this);

        String name = MyFunctions.getStringFromSharedPref(OtpActivity.this, Constants.NAME,"");
        String email = MyFunctions.getStringFromSharedPref(OtpActivity.this, Constants.EMAIL,"");


        Call<RegisterResponse> call = apiInterface.register(mobilenumber,name, email);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                MyFunctions.cancelLoading();
                RegisterResponse registerResponse = response.body();
                Toast.makeText(getApplicationContext(),registerResponse.getMessage(), Toast.LENGTH_SHORT).show();
                if(registerResponse.getSuccess()){
                    MyFunctions.saveStringToSharedPref(OtpActivity.this, Constants.USERID,registerResponse.getData().get(0).getId());
                    MyFunctions.saveStringToSharedPref(OtpActivity.this, Constants.NAME,registerResponse.getData().get(0).getName());
                    MyFunctions.saveStringToSharedPref(OtpActivity.this, Constants.EMAIL,registerResponse.getData().get(0).getEmail());
                    MyFunctions.saveStringToSharedPref(OtpActivity.this, Constants.MOBILE,registerResponse.getData().get(0).getMobile());
                    openMainActivity();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                MyFunctions.cancelLoading();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValid() {
        if(otp.length() !=6){
            otp_et.setError("Invalid OTP");
            return false;
        }
        return true;
    }
}