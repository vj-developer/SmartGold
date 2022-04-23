package com.greymatter.smartgold.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.greymatter.smartgold.R;
import com.greymatter.smartgold.model.LockedOfferResponse;
import com.greymatter.smartgold.utils.Constants;
import com.greymatter.smartgold.utils.MyFunctions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;
import java.util.Locale;

public class OfferLockAdapter extends RecyclerView.Adapter <OfferLockAdapter.ViewHolder>{
    List<LockedOfferResponse.Data> offerlockList;
    Context context;
    public OfferLockAdapter(List<LockedOfferResponse.Data> offerlockList, Context context) {
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
        LockedOfferResponse.Data offerlock = offerlockList.get(position);
        //String add_position = address.getName();
        holder.offer_lock_id.setText(offerlock.getId());
        holder.valid_date.setText(offerlock.getValidTill());
        holder.shop_name_tv.setText(offerlock.getStoreName());
        holder.shop_address_tv.setText(offerlock.getStreet());

        String wastage = offerlock.getWastage() + "% wastage";
        String gram_price = MyFunctions.ConvertToINR(offerlock.getGram_price()) + " Discount per gram";
        holder.gram_price.setText(gram_price);
        holder.wastage.setText(wastage);

        holder.order_status.setText(offerlock.getStatus());

        holder.call.setOnClickListener(v -> makePhoneCall(offerlock.getMobile()));
        holder.location.setOnClickListener(v -> openLocation(offerlock.getLatitude(),offerlock.getLongitude()));
    }

    private void openLocation(String latitude, String longitude) {
        try{
            String uri = String.format(Locale.ENGLISH, "geo:%f,%f", Float.parseFloat(latitude), Float.parseFloat(longitude));
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            context.startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void makePhoneCall(String mobile) {

        Dexter.withContext(context)
                .withPermission(Manifest.permission.CALL_PHONE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                        String number = "tel:"+mobile;
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse(number));
                        context.startActivity(callIntent);
                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                }).check();


    }

    @Override
    public int getItemCount() {
        return offerlockList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView offer_lock_id,valid_date,shop_name_tv,shop_address_tv,gram_price,wastage,order_status;
        ImageView call,location;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            offer_lock_id = itemView.findViewById(R.id.offer_lock_id);
            valid_date = itemView.findViewById(R.id.valid_date);
            shop_name_tv = itemView.findViewById(R.id.shop_name_tv);
            shop_address_tv = itemView.findViewById(R.id.shop_address_tv);
            gram_price = itemView.findViewById(R.id.gram_offer);
            wastage = itemView.findViewById(R.id.wastage);
            order_status = itemView.findViewById(R.id.order_status);
            call = itemView.findViewById(R.id.call);
            location = itemView.findViewById(R.id.location);

        }
    }
}
