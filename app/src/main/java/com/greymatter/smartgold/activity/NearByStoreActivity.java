package com.greymatter.smartgold.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.greymatter.smartgold.R;
import com.greymatter.smartgold.utils.Constants;
import com.greymatter.smartgold.utils.MyFunctions;
import com.shivtechs.maplocationpicker.LocationPickerActivity;
import com.shivtechs.maplocationpicker.MapUtility;

public class NearByStoreActivity extends AppCompatActivity {

    TextView location_tv;
    private final int ADDRESS_PICKER_REQUEST = 123;
    String latitude= "null",longitude = "null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_store);

        location_tv = findViewById(R.id.location_tv);

        MapUtility.apiKey = getResources().getString(R.string.your_api_key);

        findViewById(R.id.location_pick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LocationPickerActivity.class);
                startActivityForResult(i, ADDRESS_PICKER_REQUEST);
            }
        });


        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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

                    location_tv.setText(new StringBuilder().append
                            (completeAddress.getString("city")).append(" - ").append
                            (completeAddress.getString("postalcode")).toString());

                    latitude = String.valueOf(currentLatitude);
                    longitude = String.valueOf(currentLongitude);

                    MyFunctions.saveStringToSharedPref(getApplicationContext(), Constants.LATITUDE,latitude);
                    MyFunctions.saveStringToSharedPref(getApplicationContext(),Constants.LONGITUDE,longitude);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}