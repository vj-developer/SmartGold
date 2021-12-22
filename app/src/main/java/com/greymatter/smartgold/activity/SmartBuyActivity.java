package com.greymatter.smartgold.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.greymatter.smartgold.R;
import com.greymatter.smartgold.model.BudgetRangeResponse;
import com.greymatter.smartgold.retrofit.APIInterface;
import com.greymatter.smartgold.retrofit.RetrofitBuilder;
import com.greymatter.smartgold.utils.Constants;
import com.greymatter.smartgold.utils.MyFunctions;
import com.shivtechs.maplocationpicker.LocationPickerActivity;
import com.shivtechs.maplocationpicker.MapUtility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SmartBuyActivity extends AppCompatActivity {

    ArrayList<String> budgetRangeArray,budgetRangeIdArray;
    Spinner budgetArraySpinnner;
    private int ADDRESS_PICKER_REQUEST = 123;
    TextView location_tv;
    String latitude= "null",longitude = "null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_buy);

        budgetArraySpinnner = findViewById(R.id.budget_spinner);
        location_tv = findViewById(R.id.location_tv);

        MapUtility.apiKey = getResources().getString(R.string.your_api_key);

        latitude = MyFunctions.getStringFromSharedPref(getApplicationContext(),Constants.LATITUDE,"null");
        longitude = MyFunctions.getStringFromSharedPref(getApplicationContext(),Constants.LONGITUDE,"null");
        if (!latitude.equals("null")){
            try {
                getAddressFromLatLng(latitude,longitude);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        findViewById(R.id.budget_gram_apply_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!latitude.equals("null")){
                    openShopList();
                }
                else
                    Toast.makeText(getApplicationContext(), "Select the location", Toast.LENGTH_SHORT).show();
            }
        });

        budgetArraySpinnner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.spinner_txt));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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

        budgetRangeApi();

    }

    private void openShopList() {
        String budget_id = budgetRangeIdArray.get(budgetArraySpinnner.getSelectedItemPosition());
        Intent intent = new Intent(getApplicationContext(), ShopListActivity.class);
        intent.putExtra(Constants.BUDGET_ID,budget_id);
        intent.putExtra(Constants.LONGITUDE,longitude);
        intent.putExtra(Constants.LATITUDE,latitude);
        startActivity(intent);
    }

    private void budgetRangeApi() {

        MyFunctions.showLoading(SmartBuyActivity.this);
        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);
        Call<BudgetRangeResponse> call = apiInterface.budget_range();

        call.enqueue(new Callback<BudgetRangeResponse>() {
            @Override
            public void onResponse(Call<BudgetRangeResponse> call, Response<BudgetRangeResponse> response) {
                MyFunctions.cancelLoading();
                BudgetRangeResponse budgetRangeResponse = response.body();
                if(budgetRangeResponse.getSuccess()){
                    budgetRangeArray = new ArrayList<>();
                    budgetRangeIdArray = new ArrayList<>();
                    for(int i=0; i<budgetRangeResponse.getData().size(); i++){
                        budgetRangeArray.add(budgetRangeResponse.getData().get(i).getBudget());
                        budgetRangeIdArray.add(budgetRangeResponse.getData().get(i).getId());

                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, budgetRangeArray);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                        budgetArraySpinnner.setAdapter(spinnerArrayAdapter);

                    }
                }
            }

            @Override
            public void onFailure(Call<BudgetRangeResponse> call, Throwable t) {
                MyFunctions.cancelLoading();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
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

                    MyFunctions.saveStringToSharedPref(getApplicationContext(),Constants.LATITUDE,latitude);
                    MyFunctions.saveStringToSharedPref(getApplicationContext(),Constants.LONGITUDE,longitude);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void getAddressFromLatLng(String latitude, String longitude) throws IOException {

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();

        location_tv.setText(new StringBuilder().append
                (city).append(" - ").append
                (postalCode).toString());

    }
}