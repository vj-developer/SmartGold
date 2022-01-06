package com.greymatter.smartgold.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAddressActivity extends AppCompatActivity {

    EditText user_name_et,address_et, address_optional_et, area_et, city_et, pin_code_et;
    Button add_address;
    String user_name, address, address_optional , area, city, pin_code;
    LocationManager locationManager;
    String latitude, longitude;
    String TAG = "AddAddressActivity";

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

        findViewById(R.id.current_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLocationPermission();
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

    private void checkLocationPermission() {
        Dexter.withContext(AddAddressActivity.this).withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                if (multiplePermissionsReport.areAllPermissionsGranted()){
                    try {
                        getCurrentLocation();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

            }
        }).check();
    }

    private void getCurrentLocation() throws IOException {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            @SuppressLint("MissingPermission")
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
                Log.d(TAG,"Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);
                getAddressFromLatLng(lat,longi);
            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
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

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").
                setCancelable(false).
                setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }}).
                setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void AddressApiCall() {

        MyFunctions.showLoading(AddAddressActivity.this);
        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);
        String user_id = MyFunctions.getStringFromSharedPref(AddAddressActivity.this,Constants.USERID,"");
        Call<AddAddressResponse> call = apiInterface.add_address(user_id,user_name,address,address_optional,city,area,pin_code);
        call.enqueue(new Callback<AddAddressResponse>() {
            @Override
            public void onResponse(Call<AddAddressResponse> call, Response<AddAddressResponse> response) {
                MyFunctions.cancelLoading();
                AddAddressResponse addAddressResponse = response.body();
                Toast.makeText(getApplicationContext(), addAddressResponse.getMessage(), Toast.LENGTH_SHORT).show();
                if(addAddressResponse.getSuccess()){
                    finish();
                }
            }

            @Override
            public void onFailure(Call<AddAddressResponse> call, Throwable t) {
                MyFunctions.cancelLoading();
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