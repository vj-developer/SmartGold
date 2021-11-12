package com.greymatter.smartgold.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.greymatter.smartgold.R;
import com.greymatter.smartgold.activity.ReputedShopListActivity;
import com.greymatter.smartgold.model.BudgetRangeResponse;
import com.greymatter.smartgold.model.SliderData;
import com.greymatter.smartgold.retrofit.APIInterface;
import com.greymatter.smartgold.retrofit.RetrofitBuilder;
import com.greymatter.smartgold.utils.Constants;
import com.shivtechs.maplocationpicker.LocationPickerActivity;
import com.shivtechs.maplocationpicker.MapUtility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SmartbuyFragment extends Fragment {

    ArrayList<String> budgetRangeArray,budgetRangeIdArray;
    Spinner budgetArraySpinnner;
    private int ADDRESS_PICKER_REQUEST = 123;
    TextView location_tv;
    String latitude= "null",longitude = "null";

    public SmartbuyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_smartbuy, container, false);

        budgetArraySpinnner = view.findViewById(R.id.budget_spinner);
        location_tv = view.findViewById(R.id.location_tv);
        
        view.findViewById(R.id.budget_gram_apply_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!latitude.equals("null")){
                    openShopList();
                }
               else
                    Toast.makeText(getActivity(), "Select the location", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.location_pick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), LocationPickerActivity.class);
                startActivityForResult(i, ADDRESS_PICKER_REQUEST);
            }
        });
        budgetRangeApi();

        MapUtility.apiKey = getResources().getString(R.string.your_api_key);


        return view;
    }

    private void openShopList() {
        String budget_id = budgetRangeIdArray.get(budgetArraySpinnner.getSelectedItemPosition());
        Intent intent = new Intent(getActivity(), ReputedShopListActivity.class);
        intent.putExtra(Constants.BUDGET_ID,budget_id);
        intent.putExtra(Constants.LONGITUDE,longitude);
        intent.putExtra(Constants.LATITUDE,latitude);
        startActivity(intent);
    }

    private void budgetRangeApi() {
        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);

        Call<BudgetRangeResponse> call = apiInterface.budget_range();

        call.enqueue(new Callback<BudgetRangeResponse>() {
            @Override
            public void onResponse(Call<BudgetRangeResponse> call, Response<BudgetRangeResponse> response) {
                BudgetRangeResponse budgetRangeResponse = response.body();
                if(budgetRangeResponse.getSuccess()){

                    budgetRangeArray = new ArrayList<>();
                    budgetRangeIdArray = new ArrayList<>();
                    for(int i=0; i<budgetRangeResponse.getData().size(); i++){
                        budgetRangeArray.add(budgetRangeResponse.getData().get(i).getBudget());
                        budgetRangeIdArray.add(budgetRangeResponse.getData().get(i).getId());
                        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, budgetRangeArray);
                        budgetArraySpinnner.setAdapter(spinnerArrayAdapter)     ;               }
                }
            }

            @Override
            public void onFailure(Call<BudgetRangeResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
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
                    location_tv.setText(new StringBuilder().append("addressline2: ").append
                            (completeAddress.getString("addressline2")).append("\ncity: ").append
                            (completeAddress.getString("city")).append("\npostalcode: ").append
                            (completeAddress.getString("postalcode")).append("\nstate: ").append
                            (completeAddress.getString("state")).toString());
                    
                    latitude = String.valueOf(currentLatitude);
                    longitude = String.valueOf(currentLongitude);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}