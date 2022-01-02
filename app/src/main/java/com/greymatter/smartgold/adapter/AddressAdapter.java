package com.greymatter.smartgold.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.greymatter.smartgold.R;
import com.greymatter.smartgold.activity.AddressActivity;
import com.greymatter.smartgold.activity.CartActivity;
import com.greymatter.smartgold.activity.ProductDetailActivity;
import com.greymatter.smartgold.model.AddToCartResponse;
import com.greymatter.smartgold.model.AddressListResponse;
import com.greymatter.smartgold.retrofit.APIInterface;
import com.greymatter.smartgold.retrofit.RetrofitBuilder;
import com.greymatter.smartgold.utils.Constants;
import com.greymatter.smartgold.utils.MyFunctions;

import java.text.BreakIterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressAdapter extends RecyclerView.Adapter <AddressAdapter.ViewHolder>{
    List<AddressListResponse.Datum> addressList;
    Context context;

    public AddressAdapter(List<AddressListResponse.Datum> addressList, Context context) {
        this.addressList = addressList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AddressListResponse.Datum address = addressList.get(position);
        //String add_position = address.getName();
        String address_details = address.getAddress()+" , "+address.getArea()+ " , ";
        String pincode_city = address.getCity()+" - "+address.getPincode();
        holder.add_heading.setText(address.getName());
        holder.address.setText(address_details);
        holder.pincode.setText(pincode_city);

        if (address.getDefault_address().equals(Constants.TRUE)) holder.primary_address.setVisibility(View.VISIBLE);
        else holder.primary_address.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!address.getDefault_address().equals(Constants.TRUE)) makeAddressDefault(address.getId());
            }
        });
    }

    private void makeAddressDefault(String id) {
        MyFunctions.showLoading(context);
        String user_id = MyFunctions.getStringFromSharedPref(context,Constants.USERID,"");

        APIInterface apiInterface = RetrofitBuilder.getClient().create(APIInterface.class);
        Call<AddToCartResponse> call = apiInterface.make_address_default(user_id,id);
        call.enqueue(new Callback<AddToCartResponse>() {
            @Override
            public void onResponse(Call<AddToCartResponse> call, Response<AddToCartResponse> response) {
                MyFunctions.cancelLoading();

                if (response.isSuccessful()){
                    AddToCartResponse addToCartResponse = response.body();
                    Toast.makeText(context, addToCartResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    if(addToCartResponse.getSuccess()){
                        context.startActivity(new Intent(context, AddressActivity.class));
                        ((Activity)context).finish();
                    }

                }else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<AddToCartResponse> call, Throwable t) {
                MyFunctions.cancelLoading();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView pincode, add_heading, address;
        ImageView primary_address;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           pincode = itemView.findViewById(R.id.pincode);
           add_heading = itemView.findViewById(R.id.address_heading);
           address = itemView.findViewById(R.id.address);
           primary_address = itemView.findViewById(R.id.primary_address);
        }
    }

}
