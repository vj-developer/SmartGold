package com.greymatter.smartgold.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.greymatter.smartgold.R;
import com.greymatter.smartgold.adapter.OfferLockAdapter;
import com.greymatter.smartgold.adapter.ProductsAdapter;
import com.greymatter.smartgold.model.OfferLockResponse;
import com.greymatter.smartgold.model.ProductListResponse;
import com.greymatter.smartgold.retrofit.APIInterface;
import com.greymatter.smartgold.retrofit.RetrofitBuilder;
import com.greymatter.smartgold.utils.Constants;
import com.greymatter.smartgold.utils.MyFunctions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewOfferLockActivity extends AppCompatActivity {
    RecyclerView offer_lock_recycler;
    OfferLockAdapter offerlockAdapter;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_offer_lock);
        user_id = MyFunctions.getStringFromSharedPref(getApplicationContext(), Constants.USERID,"null");

        offer_lock_recycler = findViewById(R.id.recyclerviewofferlock);

        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        OfferLockedList();
    }

    private void OfferLockedList() {
        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);
        Call<OfferLockResponse> call = apiInterface.offer_locked(user_id);
        call.enqueue(new Callback<OfferLockResponse>() {
            @Override
            public void onResponse(Call<OfferLockResponse> call, Response<OfferLockResponse> response) {
                OfferLockResponse productResponse = response.body();
                if(productResponse.getSuccess()){
                    offerlockAdapter = new OfferLockAdapter(productResponse.getData(),ViewOfferLockActivity.this);
                    offer_lock_recycler.setAdapter(offerlockAdapter);
                }

            }

            @Override
            public void onFailure(Call<OfferLockResponse> call, Throwable t) {
                Toast.makeText(ViewOfferLockActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}