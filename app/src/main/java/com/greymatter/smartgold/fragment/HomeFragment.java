package com.greymatter.smartgold.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.greymatter.smartgold.FilterActivity;
import com.greymatter.smartgold.MainActivity;
import com.greymatter.smartgold.R;
import com.greymatter.smartgold.adapter.SliderAdapter;
import com.greymatter.smartgold.model.BannerListResponse;
import com.greymatter.smartgold.model.SliderData;
import com.greymatter.smartgold.retrofit.APIInterface;
import com.greymatter.smartgold.retrofit.RetrofitBuilder;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    SliderView sliderView;
    ArrayList<SliderData> sliderDataArrayList;
    SmartbuyFragment smartbuyFragment = new SmartbuyFragment();

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        sliderDataArrayList = new ArrayList<>();

        sliderView = view.findViewById(R.id.banner_slider);
        bannerSliderApi();
        view.findViewById(R.id.filter_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FilterActivity.class);
                startActivity(intent);
            }
        });
        /*view.findViewById(R.id.Smartbuy_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogButtonClicked(view);
            }
        });*/



        return view;
    }
    /*public void showAlertDialogButtonClicked(View view)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View customLayout = getLayoutInflater().inflate(R.layout.smartbuy_location, null);
        builder.setView(customLayout);
        AlertDialog dialog = builder.create();
        dialog.show();
        customLayout.findViewById(R.id.location_submit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.fragmentManager.beginTransaction().replace(R.id.container,smartbuyFragment).addToBackStack(null).commit();
                dialog.dismiss();
            }
        });

    }*/

    private void viewBanner() {
        SliderAdapter adapter = new SliderAdapter(getActivity(), sliderDataArrayList);
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(adapter);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
    }

    private void bannerSliderApi() {
        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);

        Call<BannerListResponse> call = apiInterface.banner_list();
        call.enqueue(new Callback<BannerListResponse>() {
            @Override
            public void onResponse(Call<BannerListResponse> call, Response<BannerListResponse> response) {

                BannerListResponse bannerListResponse = response.body();
                if(bannerListResponse.getSuccess()){

                    for (int i=0;i<bannerListResponse.getData().size();i++){
                        SliderData sliderData = new SliderData(bannerListResponse.getData().get(i).getImgUrl());
                        sliderDataArrayList.add(sliderData);
                    }
                    viewBanner();
                }

            }

            @Override
            public void onFailure(Call<BannerListResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}