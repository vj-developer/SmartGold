package com.greymatter.smartgold.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.adevinta.leku.LocationPickerActivity;
import com.google.gson.Gson;
import com.greymatter.smartgold.R;
import com.greymatter.smartgold.model.AddAddressResponse;
import com.greymatter.smartgold.model.AddressListResponse;
import com.greymatter.smartgold.retrofit.APIInterface;
import com.greymatter.smartgold.retrofit.ApiConfig;
import com.greymatter.smartgold.retrofit.RetrofitBuilder;
import com.greymatter.smartgold.utils.Constants;
import com.greymatter.smartgold.utils.MyFunctions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.shivtechs.maplocationpicker.MapUtility;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAddressActivity extends AppCompatActivity {

    private static final int ADDRESS_PICKER_REQUEST = 12;
    private static final int MAP_BUTTON_REQUEST_CODE = 1;
    EditText user_name_et,address_et, address_optional_et, area_et, city_et, pin_code_et;
    Button add_address;
    String user_name, address, address_optional , area, city, pin_code;
    LocationManager locationManager;
    String latitude, longitude;
    String TAG = "EditAddressActivity";
    private String address_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);

        user_name_et = findViewById(R.id.name);
        address_et = findViewById(R.id.address);
        address_optional_et = findViewById(R.id.address_optional);
        area_et = findViewById(R.id.area);
        city_et = findViewById(R.id.city);
        pin_code_et = findViewById(R.id.pincode);
        add_address = findViewById(R.id.add_address_btn);

        MapUtility.apiKey = getResources().getString(R.string.your_api_key);
        
        prefillAddress();
        
        findViewById(R.id.select_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(EditAddressActivity.this, LocationPickerActivity.class);
                startActivityForResult(i, ADDRESS_PICKER_REQUEST);*/
                Intent i = new LocationPickerActivity.Builder()
                        .withGooglePlacesApiKey(getResources().getString(R.string.your_api_key))
                        .withLegacyLayout()
                        .withGeolocApiKey(getResources().getString(R.string.your_api_key))
                        .build(EditAddressActivity.this);
                startActivityForResult(i,MAP_BUTTON_REQUEST_CODE);
            }
        });
        
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
                    editAddressApiCall();
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

    private void editAddressApiCall() {
        MyFunctions.showLoading(EditAddressActivity.this);
        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);
        String user_id = MyFunctions.getStringFromSharedPref(EditAddressActivity.this,Constants.USERID,"");
        Call<AddAddressResponse> call = apiInterface.edit_address(ApiConfig.SecurityKey,Constants.AccessKeyVal,address_id,user_id,user_name,address,address_optional,city,area,pin_code);
        call.enqueue(new Callback<AddAddressResponse>() {
            @Override
            public void onResponse(Call<AddAddressResponse> call, Response<AddAddressResponse> response) {
                MyFunctions.cancelLoading();

                if (response.isSuccessful()){

                    AddAddressResponse addAddressResponse = response.body();
                    Toast.makeText(getApplicationContext(), addAddressResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    if(addAddressResponse.getSuccess()){
                        finish();
                    }

                }else {
                    Toast.makeText(getApplicationContext(), Constants.API_ERROR, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<AddAddressResponse> call, Throwable t) {
                MyFunctions.cancelLoading();
                Toast.makeText(getApplicationContext(), Constants.API_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void prefillAddress() {
        AddressListResponse.Datum datum = new Gson().fromJson(getIntent().getStringExtra(Constants.ADDRESS),AddressListResponse.Datum.class);
        user_name_et.setText(datum.getName());
        address_et.setText(datum.getAddress());
        address_optional_et.setText(datum.getLandmark());
        area_et.setText(datum.getArea());
        city_et.setText(datum.getCity());
        pin_code_et.setText(datum.getPincode());

        address_id = datum.getId();
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
    
    private void getAddressFromLatLng(double lat, double longi) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(lat, longi, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String subLocality = addresses.get(0).getSubLocality();
        String subThoroughfare = addresses.get(0).getSubThoroughfare();
        String thoroughfare = addresses.get(0).getThoroughfare();
        String premises = addresses.get(0).getPremises();
        String locale = addresses.get(0).getLocale().getDisplayName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();

        Log.d(TAG,subLocality+" , "+subThoroughfare+" , "+premises+" ,"+locale);
        address_et.setText(knownName);
        area_et.setText(address);
        city_et.setText(city);
        pin_code_et.setText(postalCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADDRESS_PICKER_REQUEST) {
            try {
                if (data != null && data.getStringExtra(MapUtility.ADDRESS) != null) {
                    // String address = data.getStringExtra(MapUtility.ADDRESS);
                    double currentLatitude = data.getDoubleExtra(MapUtility.LATITUDE, 0.0);
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

                    getAddressFromLatLng(currentLatitude,currentLongitude);
                    latitude = String.valueOf(currentLatitude);
                    longitude = String.valueOf(currentLongitude);

                    MyFunctions.saveStringToSharedPref(getApplicationContext(), Constants.LATITUDE,latitude);
                    MyFunctions.saveStringToSharedPref(getApplicationContext(),Constants.LONGITUDE,longitude);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (resultCode == Activity.RESULT_OK && data != null) {

            if (requestCode == MAP_BUTTON_REQUEST_CODE) {
                double currentLatitude = data.getDoubleExtra("latitude", 0.0);
                double currentLongitude = data.getDoubleExtra("longitude", 0.0);

                try {
                    getAddressFromLatLng(currentLatitude,currentLongitude);
                } catch (IOException e) {
                    e.printStackTrace();
                }

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