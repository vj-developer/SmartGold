package com.greymatter.smartgold.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.greymatter.smartgold.R;
import com.greymatter.smartgold.activity.ShopOfferDetailsActivity;
import com.greymatter.smartgold.model.SmartOffersResponse;
import com.greymatter.smartgold.utils.Constants;
import com.greymatter.smartgold.utils.MyFunctions;

import java.util.List;

public class SmartOffersAdapter extends RecyclerView.Adapter <SmartOffersAdapter.ViewHolder> {
    List<SmartOffersResponse.Datum> smartOffer;
    Context context;

    public SmartOffersAdapter(List<SmartOffersResponse.Datum> smartOffer, Context context) {
        this.smartOffer = smartOffer;
        this.context = context;
    }

    @NonNull
    @Override
    public SmartOffersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_reputed_shop,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SmartOffersAdapter.ViewHolder holder, int position) {
        SmartOffersResponse.Datum shop = smartOffer.get(position);
        String wastage = shop.getWastage() + "% wastage";
        String members_locked = shop.getMaxLocked() + " locked";
        String distance = "Near "+shop.getDistance() + " km";
        String gram_price = Constants.PER_GRAM_PRICE+MyFunctions.ConvertToINR(shop.getGramPrice());
        holder.shopNickName.setText(shop.getNickName());
        holder.gram_price.setText(gram_price);
        holder.wastage.setText(wastage);
        holder.members_locked.setText(members_locked);
        holder.distance.setText(distance);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String smartOffer = new Gson().toJson(shop);

                Intent intent = new Intent(context, ShopOfferDetailsActivity.class);
                intent.putExtra(Constants.NICKNAME,shop.getNickName());
                intent.putExtra(Constants.GRAMPRICE,shop.getGramPrice());
                intent.putExtra(Constants.WASTAGE,shop.getWastage());
                intent.putExtra(Constants.MAXLOCKED,shop.getMaxLocked());
                intent.putExtra(Constants.DISTANCE,shop.getDistance());
                intent.putExtra(Constants.SELLER_ID,shop.getSeller_id());
                intent.putExtra(Constants.OFFER_ID,shop.getId());
                intent.putExtra(Constants.SMART_OFFER,smartOffer);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return smartOffer.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView shopNickName, gram_price, wastage, members_locked,distance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shopNickName = itemView.findViewById(R.id.shop_heading);
            gram_price = itemView.findViewById(R.id.gram_offer);
            wastage = itemView.findViewById(R.id.wastage);
            members_locked = itemView.findViewById(R.id.member_locked);
            distance = itemView.findViewById(R.id.distance);
        }
    }

}