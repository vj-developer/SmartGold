package com.greymatter.smartgold.activity;

import static com.shivtechs.maplocationpicker.MapUtility.ADDRESS;
import static com.shivtechs.maplocationpicker.MapUtility.LATITUDE;
import static com.shivtechs.maplocationpicker.MapUtility.LONGITUDE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.adevinta.leku.LocationPickerActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;
import com.greymatter.smartgold.R;
import com.greymatter.smartgold.adapter.StoreAdapter;
import com.greymatter.smartgold.model.StoreResponse;
import com.greymatter.smartgold.retrofit.APIInterface;
import com.greymatter.smartgold.retrofit.ApiConfig;
import com.greymatter.smartgold.retrofit.RetrofitBuilder;
import com.greymatter.smartgold.utils.Constants;
import com.greymatter.smartgold.utils.MyFunctions;
import com.shivtechs.maplocationpicker.MapUtility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NearByStoreActivity extends AppCompatActivity {

    private static final int MAP_BUTTON_REQUEST_CODE = 1;
    TextView location_tv;
    private final int ADDRESS_PICKER_REQUEST = 123;
    String latitude= null,longitude = null,range_to=null,address;
    StoreAdapter storeAdapter;
    List<StoreResponse.Datum> data = new ArrayList<>();
    RecyclerView store_recycler;
    EditText search_bar_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_store);

        store_recycler = findViewById(R.id.store_recycler);
        search_bar_et = findViewById(R.id.search_bar_et);

        MapUtility.apiKey = getResources().getString(R.string.your_api_key);

        storeAdapter = new StoreAdapter(data,NearByStoreActivity.this);
        store_recycler.setAdapter(storeAdapter);

        findViewById(R.id.filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet();
            }
        });

        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getShopList();

        search_bar_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterStore(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void filterStore(String key) {
        List<StoreResponse.Datum> stores = new ArrayList<>();
        for (int i=0;i<data.size();i++){
            if (data.get(i).getStoreName().toLowerCase().contains(key)){
                stores.add(data.get(i));
            }
        }
        storeAdapter.filterByName(stores);
    }

    private void getShopList() {
        data.clear();
        MyFunctions.showLoading(NearByStoreActivity.this);
        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);
        Call<StoreResponse> call = apiInterface.getSellers(ApiConfig.SecurityKey,Constants.AccessKeyVal,latitude,longitude,range_to);
        call.enqueue(new Callback<StoreResponse>() {
            @Override
            public void onResponse(Call<StoreResponse> call, Response<StoreResponse> response) {
                MyFunctions.cancelLoading();
                StoreResponse productResponse = response.body();
                if(productResponse.getSuccess()){
                    data.addAll(productResponse.getData());
                }
                else {
                    Toast.makeText(getApplicationContext(), productResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
                storeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<StoreResponse> call, Throwable t) {
                MyFunctions.cancelLoading();
                Toast.makeText(getApplicationContext(), Constants.API_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showBottomSheet() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);

        location_tv = bottomSheetDialog.findViewById(R.id.location_tv);
        Slider slider = bottomSheetDialog.findViewById(R.id.slider);

        if (latitude!=null){
            location_tv.setText(address);
        }
        if (range_to!=null){
            slider.setValue(Float.parseFloat(range_to));
        }

        /*Range upto*/
        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                //end price     default = 200
                range_to = String.valueOf(Math.round(value));
                //end_km.setText(to_km +"km");
            }
        });
        slider.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                return Math.round(value)+"km";
            }
        });

        bottomSheetDialog.findViewById(R.id.location_pick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(NearByStoreActivity.this, LocationPickerActivity.class);
                startActivityForResult(i, ADDRESS_PICKER_REQUEST);*/

                Intent i = new LocationPickerActivity.Builder()
                        .withGooglePlacesApiKey(getResources().getString(R.string.your_api_key))
                        .withLegacyLayout()
                        .withGeolocApiKey(getResources().getString(R.string.your_api_key))
                        .build(NearByStoreActivity.this);
                startActivityForResult(i,MAP_BUTTON_REQUEST_CODE);

            }
        });

        bottomSheetDialog.findViewById(R.id.apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latitude == null){
                    Toast.makeText(NearByStoreActivity.this, "Select your location", Toast.LENGTH_SHORT).show();
                }else {
                    bottomSheetDialog.dismiss();
                    getShopList();
                }

            }
        });

        bottomSheetDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {

            if (requestCode == ADDRESS_PICKER_REQUEST) {
                try {
                    if (data != null && data.getStringExtra(MapUtility.ADDRESS) != null) {
                        // String address = data.getStringExtra(MapUtility.ADDRESS);
                        double currentLatitude = data.getDoubleExtra(LATITUDE, 0.0);
                        double currentLongitude = data.getDoubleExtra(MapUtility.LONGITUDE, 0.0);
                        Bundle completeAddress =data.getBundleExtra("fullAddress");
                    /* data in completeAddress bundle
                    "fulladdress"
                    "city"
                    "state"
                    "postalcode"
                    "country"
                    "addressline1"
                    "addressline2"
                     */

                        location_tv.setText(new StringBuilder().append
                                (completeAddress.getString("city")).append(" - ").append
                                (completeAddress.getString("postalcode")).toString());

                        address = location_tv.getText().toString().trim();

                        latitude = String.valueOf(currentLatitude);
                        longitude = String.valueOf(currentLongitude);

                        MyFunctions.saveStringToSharedPref(getApplicationContext(), Constants.LATITUDE,latitude);
                        MyFunctions.saveStringToSharedPref(getApplicationContext(),Constants.LONGITUDE,longitude);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            if (requestCode == MAP_BUTTON_REQUEST_CODE) {
                double currentLatitude = data.getDoubleExtra("latitude", 0.0);
                double currentLongitude = data.getDoubleExtra("longitude", 0.0);

                location_tv.setText(data.getStringExtra("location_address"));

                address = location_tv.getText().toString().trim();

                latitude = String.valueOf(currentLatitude);
                longitude = String.valueOf(currentLongitude);

                MyFunctions.saveStringToSharedPref(getApplicationContext(), Constants.LATITUDE,latitude);
                MyFunctions.saveStringToSharedPref(getApplicationContext(),Constants.LONGITUDE,longitude);
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            Log.d("RESULT****", "CANCELLED");
        }
    }

}