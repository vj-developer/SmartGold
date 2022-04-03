package com.greymatter.smartgold.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.greymatter.smartgold.R;
import com.greymatter.smartgold.activity.CategoryListActivity;
import com.greymatter.smartgold.activity.ProductDetailActivity;
import com.greymatter.smartgold.activity.ProductListActivity;
import com.greymatter.smartgold.model.ProductListResponse;
import com.greymatter.smartgold.model.StoreResponse;
import com.greymatter.smartgold.utils.Constants;
import com.greymatter.smartgold.utils.MyFunctions;

import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter <StoreAdapter.ViewHolder>{
    List<StoreResponse.Datum> data;
    Context context;

    public StoreAdapter(List<StoreResponse.Datum> productList, Context context) {
        this.data = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StoreResponse.Datum datum = data.get(position);
        //String add_position = address.getName();

        holder.name.setText(datum.getStoreName());
        Glide.with(holder.itemView)
                .load(datum.getLogo())
                .fitCenter()
                .into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jsonString = new Gson().toJson(datum);
                context.startActivity(new Intent(context, ProductListActivity.class)
                        .putExtra(Constants.STORE,jsonString)
                        .putExtra(Constants.SCREEN_TYPE,Constants.STORE)
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void filterByName(List<StoreResponse.Datum> stores) {
        this.data = stores;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           image = itemView.findViewById(R.id.shop_logo);
           name = itemView.findViewById(R.id.store_name);
        }
    }

}
