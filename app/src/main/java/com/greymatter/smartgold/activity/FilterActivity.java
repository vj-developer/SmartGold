package com.greymatter.smartgold.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.greymatter.smartgold.R;
import com.greymatter.smartgold.model.CategoryResponse;
import com.greymatter.smartgold.retrofit.APIInterface;
import com.greymatter.smartgold.retrofit.ApiConfig;
import com.greymatter.smartgold.retrofit.RetrofitBuilder;
import com.greymatter.smartgold.utils.Constants;
import com.greymatter.smartgold.utils.MyFunctions;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;

import it.mirko.rangeseekbar.OnRangeSeekBarListener;
import it.mirko.rangeseekbar.RangeSeekBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterActivity extends AppCompatActivity implements OnRangeSeekBarListener {

    TextView start_price,end_price;
    Spinner category_spinner,sort_spinner,gender_spinner,weight_spinner;
    ArrayList<String> categoryArray,categoryIdArray;
    String from_price="50000",to_price="1000000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        RangeSeekBar rangeSeekBar = findViewById(R.id.rangeSeekBar);
        start_price = findViewById(R.id.start_price);
        end_price = findViewById(R.id.end_price);
        category_spinner = findViewById(R.id.category_spinner);
        sort_spinner = findViewById(R.id.sort_spinner);
        gender_spinner = findViewById(R.id.gender_spinner);
        weight_spinner = findViewById(R.id.weight_spinner);

        rangeSeekBar.setOnRangeSeekBarListener(this);
        rangeSeekBar.setMinDifference(5);
        rangeSeekBar.setStartProgress(5); // default is 0
        rangeSeekBar.setEndProgress(100); // default is 50*/

        CategoryList();

        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.spinner_txt));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String order_by = "1";
                if (sort_spinner.getSelectedItem().toString().equals("Price: Low to High")){
                    order_by = "1";
                }else {
                    order_by = "2";
                }
                startActivity(new Intent(getApplicationContext(),FilteredProductsActivity.class)
                        .putExtra(Constants.CATEGORY_ID, categoryIdArray.get(category_spinner.getSelectedItemPosition()))
                        .putExtra(Constants.FROM_PRICE_RANGE, from_price)
                        .putExtra(Constants.TO_PRICE_RANGE, to_price)
                        .putExtra(Constants.ORDER_BY,order_by )
                        .putExtra(Constants.SEARCH_TERM,"null" )
                        .putExtra(Constants.GENDER,gender_spinner.getSelectedItem().toString())
                        .putExtra(Constants.WEIGHT,weight_spinner.getSelectedItem().toString())
                );


            }
        });
    }

    private void CategoryList() {
        MyFunctions.showLoading(FilterActivity.this);
        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);

        Call<CategoryResponse> call = apiInterface.category(ApiConfig.SecurityKey,Constants.AccessKeyVal);
        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                MyFunctions.cancelLoading();
                CategoryResponse categoryResponse = response.body();
                if(categoryResponse.getSuccess()){

                    categoryArray = new ArrayList<>();
                    categoryIdArray = new ArrayList<>();
                    for(int i=0; i<categoryResponse.getData().size(); i++){
                        categoryArray.add(categoryResponse.getData().get(i).getName());
                        categoryIdArray.add(categoryResponse.getData().get(i).getId());

                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, categoryArray);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                        category_spinner.setAdapter(spinnerArrayAdapter);

                    }

                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                MyFunctions.cancelLoading();
                Toast.makeText(getApplicationContext(),Constants.API_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onRangeValues(RangeSeekBar rangeSeekBar, int start, int end) {

        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance(Constants.INR));

        //start price    default = 0
        from_price = String.valueOf(start*10000);
        String sp = format.format(start*10000);
        start_price.setText(sp);

        //end price     default = 100
        to_price = String.valueOf(end*10000);
        String ep = format.format(end*10000);
        end_price.setText(ep);

    }

}