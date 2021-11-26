package com.greymatter.smartgold.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.greymatter.smartgold.activity.AddressActivity;
import com.greymatter.smartgold.activity.FilterActivity;
import com.greymatter.smartgold.R;
import com.greymatter.smartgold.activity.SmartBuyActivity;
import com.greymatter.smartgold.adapter.AddressAdapter;
import com.greymatter.smartgold.adapter.CategoryAdapter;
import com.greymatter.smartgold.adapter.ProductsAdapter;
import com.greymatter.smartgold.adapter.SliderAdapter;
import com.greymatter.smartgold.model.AddressListResponse;
import com.greymatter.smartgold.model.BannerListResponse;
import com.greymatter.smartgold.model.CategoryResponse;
import com.greymatter.smartgold.model.ProductListResponse;
import com.greymatter.smartgold.model.SliderData;
import com.greymatter.smartgold.retrofit.APIInterface;
import com.greymatter.smartgold.retrofit.RetrofitBuilder;
import com.greymatter.smartgold.utils.Constants;
import com.greymatter.smartgold.utils.MyFunctions;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    SliderView sliderView;
    ArrayList<SliderData> sliderDataArrayList;
    RecyclerView category_recycler;
    RecyclerView product_recycler;
    CategoryAdapter categoryAdapter;
    ProductsAdapter productAdapter;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        sliderDataArrayList = new ArrayList<>();

        sliderView = view.findViewById(R.id.banner_slider);
        category_recycler = view.findViewById(R.id.recyclerviewcategory);
        product_recycler = view.findViewById(R.id.recyclerviewproduct);
        bannerSliderApi();
        CategoryList();
        ProductList();

        view.findViewById(R.id.filter_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FilterActivity.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.smartbuy_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SmartBuyActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void ProductList() {
        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);
        Call<ProductListResponse> call = apiInterface.product();
        call.enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
                ProductListResponse productResponse = response.body();
                if(productResponse.getSuccess()){
                    productAdapter = new ProductsAdapter(productResponse.getData(),getActivity());
                    product_recycler.setAdapter(productAdapter);
                }

            }

            @Override
            public void onFailure(Call<ProductListResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("PRODUCTRESPONSE",String.valueOf(t.getMessage()));
            }
        });

    }

    private void CategoryList() {
        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);

        Call<CategoryResponse> call = apiInterface.category();
        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                CategoryResponse categoryResponse = response.body();
                if(categoryResponse.getSuccess()){
                    categoryAdapter = new CategoryAdapter(categoryResponse.getData(),getActivity());
                    category_recycler.setAdapter(categoryAdapter);
                }

            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

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
                sliderDataArrayList.clear();
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