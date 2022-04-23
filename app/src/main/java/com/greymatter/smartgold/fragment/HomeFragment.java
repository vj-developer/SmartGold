package com.greymatter.smartgold.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.greymatter.smartgold.activity.AddAddressActivity;
import com.greymatter.smartgold.activity.CategoryListActivity;
import com.greymatter.smartgold.activity.FilterActivity;
import com.greymatter.smartgold.R;
import com.greymatter.smartgold.activity.FilteredProductsActivity;
import com.greymatter.smartgold.activity.NearByStoreActivity;
import com.greymatter.smartgold.activity.SmartBuyActivity;
import com.greymatter.smartgold.adapter.CategoryAdapter;
import com.greymatter.smartgold.adapter.ProductsAdapter;
import com.greymatter.smartgold.adapter.StoreAdapter;
import com.greymatter.smartgold.adapter.SliderAdapter;
import com.greymatter.smartgold.model.BannerListResponse;
import com.greymatter.smartgold.model.CategoryResponse;
import com.greymatter.smartgold.model.ProductListResponse;
import com.greymatter.smartgold.model.SliderData;
import com.greymatter.smartgold.model.StoreResponse;
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
import com.smarteist.autoimageslider.SliderView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    SliderView sliderView;
    ArrayList<SliderData> sliderDataArrayList;
    RecyclerView category_recycler;
    RecyclerView product_recycler,store_recycler;
    CategoryAdapter categoryAdapter;
    ProductsAdapter productAdapter;
    EditText search_bar_et;
    StoreAdapter storeAdapter;
    LocationManager locationManager;
    String latitude, longitude;
    String TAG = "Home";

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
        store_recycler = view.findViewById(R.id.store_recycler);
        search_bar_et = view.findViewById(R.id.search_bar_et);

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

        view.findViewById(R.id.nearby_store).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NearByStoreActivity.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.view_more_category).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CategoryListActivity.class);
                startActivity(intent);
            }
        });

        search_bar_et.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //do here your stuff

                    startActivity(new Intent(getActivity(), FilteredProductsActivity.class)
                            .putExtra(Constants.SEARCH_TERM, search_bar_et.getText().toString().trim())
                    );

                    return true;
                }
                return false;
            }
        });

        checkLocationPermission();

        return view;
    }

    private void checkLocationPermission() {
        Dexter.withContext(getActivity()).withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
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
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
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
                getShopList();
            } else {
                //Toast.makeText(getActivity(), "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

    private void getShopList() {
        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);
        Call<StoreResponse> call = apiInterface.getSellers(ApiConfig.SecurityKey,Constants.AccessKeyVal,latitude,longitude,"200");
        call.enqueue(new Callback<StoreResponse>() {
            @Override
            public void onResponse(Call<StoreResponse> call, Response<StoreResponse> response) {
                StoreResponse productResponse = response.body();
                if(productResponse.getSuccess()){
                    storeAdapter = new StoreAdapter(productResponse.getData(),getActivity());
                    store_recycler.setAdapter(storeAdapter);
                }
            }

            @Override
            public void onFailure(Call<StoreResponse> call, Throwable t) {

            }
        });
    }

    private void ProductList() {
        MyFunctions.showLoading(getActivity());
        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);
        Call<ProductListResponse> call = apiInterface.product(ApiConfig.SecurityKey,Constants.AccessKeyVal);
        call.enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
                MyFunctions.cancelLoading();

                ProductListResponse productResponse = response.body();
                if(productResponse.getSuccess()){
                    productAdapter = new ProductsAdapter(productResponse.getData(),getActivity());
                    product_recycler.setAdapter(productAdapter);
                }
                else {
                    Toast.makeText(getActivity(), productResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ProductListResponse> call, Throwable t) {
                MyFunctions.cancelLoading();
                Toast.makeText(getActivity(), Constants.API_ERROR, Toast.LENGTH_SHORT).show();
                Log.d("PRODUCTRESPONSE",String.valueOf(t.getMessage()));
            }
        });
    }

    private void CategoryList() {
        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);

        Call<CategoryResponse> call = apiInterface.category(ApiConfig.SecurityKey,Constants.AccessKeyVal);
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
                Toast.makeText(getActivity(), Constants.API_ERROR, Toast.LENGTH_SHORT).show();
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

        Call<BannerListResponse> call = apiInterface.banner_list(ApiConfig.SecurityKey,Constants.AccessKeyVal);
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
                Toast.makeText(getActivity(), Constants.API_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }
}