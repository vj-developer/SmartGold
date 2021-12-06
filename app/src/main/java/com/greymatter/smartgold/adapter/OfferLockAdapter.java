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
import com.greymatter.smartgold.R;
import com.greymatter.smartgold.activity.CategoryDetailActivity;
import com.greymatter.smartgold.activity.ProductListActivity;
import com.greymatter.smartgold.model.OfferLockResponse;
import com.greymatter.smartgold.utils.Constants;

import java.util.List;

public class OfferLockAdapter extends RecyclerView.Adapter <OfferLockAdapter.ViewHolder>{
    List<OfferLockResponse.Datum> offerlockList;
    Context context;
    public OfferLockAdapter(List<OfferLockResponse.Datum> offerlockList, Context context) {
        this.offerlockList = offerlockList;
        this.context = context;
    }
    @NonNull
    @Override
    public OfferLockAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_lock_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferLockAdapter.ViewHolder holder, int position) {
        OfferLockResponse.Datum offerlock = offerlockList.get(position);
        //String add_position = address.getName();
        holder.offer_lock_id.setText(offerlock.getId());
        holder.valid_date.setText(offerlock.getValidTill());
        holder.seller_details.setText(offerlock.getStoreName() + ","+offerlock.getStreet() +","+offerlock.getCity()+","+offerlock.getPincode()+","+offerlock.getState()+","+offerlock.getMobile());

    }

    @Override
    public int getItemCount() {
        return offerlockList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView offer_lock_id,valid_date,seller_details;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            offer_lock_id = itemView.findViewById(R.id.offer_lock_id);
            valid_date = itemView.findViewById(R.id.valid_date);
            seller_details = itemView.findViewById(R.id.seller_details);

        }
    }
}
