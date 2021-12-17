package com.greymatter.smartgold.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.greymatter.smartgold.R;
import com.greymatter.smartgold.activity.CartActivity;
import com.greymatter.smartgold.activity.CheckoutActivity;
import com.greymatter.smartgold.adapter.CartAdapter;
import com.greymatter.smartgold.model.AddToCartResponse;
import com.greymatter.smartgold.model.CartListResponse;
import com.greymatter.smartgold.retrofit.APIInterface;
import com.greymatter.smartgold.retrofit.RetrofitBuilder;
import com.greymatter.smartgold.utils.Constants;
import com.greymatter.smartgold.utils.MyFunctions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartFragment extends Fragment {

    CartAdapter cartAdapter;
    RecyclerView cart_recycler;
    private String Tag = "CartFragment";
    ImageView cart_empty;
    RelativeLayout cart_container;
    private List<CartListResponse.Datum> cartArrayList = new ArrayList<>();
    TextView total;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        cart_recycler = view.findViewById(R.id.cart_recycler);
        cart_empty = view.findViewById(R.id.cart_empty);
        cart_container = view.findViewById(R.id.cart_container);
        total = view.findViewById(R.id.total);

        cartList();

        cartAdapter = new CartAdapter(cartArrayList, getActivity(), new CartAdapter.OnQuantityChangedListener() {
            @Override
            public void quantityChanged(String product_id, String quantity) {
                addToCart(product_id,quantity);
                Log.d(Tag,quantity);
            }

            @Override
            public void remove(String cart_id) {
                removeFromCart(cart_id);
            }
        });
        cart_recycler.setAdapter(cartAdapter);

        view.findViewById(R.id.proceed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CheckoutActivity.class));
            }
        });

        return view;
    }

    public void cartList() {
        MyFunctions.showLoading(getActivity());
        String user_id = MyFunctions.getStringFromSharedPref(getActivity(), Constants.USERID,"");

        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);
        Call<CartListResponse> call = apiInterface.cart_list(user_id);
        call.enqueue(new Callback<CartListResponse>() {
            @Override
            public void onResponse(Call<CartListResponse> call, Response<CartListResponse> response) {
                MyFunctions.cancelLoading();
                if (response.isSuccessful()){
                    cartArrayList.clear();
                    CartListResponse cartListResponse = response.body();
                    if(cartListResponse.getSuccess()){

                        cart_empty.setVisibility(View.GONE);
                        cart_container.setVisibility(View.VISIBLE);

                        cartArrayList.addAll(cartListResponse.getData());
                        cartAdapter.notifyDataSetChanged();

                        calculateTotal();
                    }else {
                        cart_empty.setVisibility(View.VISIBLE);
                        cart_container.setVisibility(View.GONE);
                    }

                }else {
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CartListResponse> call, Throwable t) {
                MyFunctions.cancelLoading();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void calculateTotal() {

        int total_amount=0;
        for (int i=0;i<cartArrayList.size();i++){
            total_amount += cartArrayList.get(i).getDiscountedPrice();
        }

        total.setText(MyFunctions.ConvertToINR(String.valueOf(total_amount)));
    }

    private void addToCart(String product_id,String quantity) {
        MyFunctions.showLoading(getActivity());
        String user_id = MyFunctions.getStringFromSharedPref(getActivity(),Constants.USERID,"");

        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);
        Call<AddToCartResponse> call = apiInterface.add_to_cart(user_id,product_id, String.valueOf(quantity));
        call.enqueue(new Callback<AddToCartResponse>() {
            @Override
            public void onResponse(Call<AddToCartResponse> call, Response<AddToCartResponse> response) {
                MyFunctions.cancelLoading();
                if (response.isSuccessful()){

                    AddToCartResponse addToCartResponse = response.body();
                    if(addToCartResponse.getSuccess()){
                        cartList();
                    }else {
                        Toast.makeText(getActivity(), addToCartResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<AddToCartResponse> call, Throwable t) {
                MyFunctions.cancelLoading();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void removeFromCart(String cart_id) {
        MyFunctions.showLoading(getActivity());
        String user_id = MyFunctions.getStringFromSharedPref(getActivity(), Constants.USERID,"");

        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);
        Call<AddToCartResponse> call = apiInterface.remove_from_cart(user_id,cart_id);
        call.enqueue(new Callback<AddToCartResponse>() {
            @Override
            public void onResponse(Call<AddToCartResponse> call, Response<AddToCartResponse> response) {
                MyFunctions.cancelLoading();

                if (response.isSuccessful()){
                    AddToCartResponse addToCartResponse = response.body();
                    if(addToCartResponse.getSuccess()){
                        cartList();
                    }else {
                        Toast.makeText(getActivity(), addToCartResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddToCartResponse> call, Throwable t) {
                MyFunctions.cancelLoading();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}