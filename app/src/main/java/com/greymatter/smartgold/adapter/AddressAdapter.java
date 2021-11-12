package com.greymatter.smartgold.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.greymatter.smartgold.R;
import com.greymatter.smartgold.model.AddressListResponse;

import java.text.BreakIterator;
import java.util.List;

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
        String address_details = address.getAddress()+" , "+ address.getLandmark()+" , "+address.getArea()+ " , ";
        String pincode_city = address.getCity()+" - "+address.getPincode();
        holder.add_heading.setText(address.getName());
        holder.address.setText(address_details);
        holder.pincode.setText(pincode_city);
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView pincode, add_heading, address;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           pincode = itemView.findViewById(R.id.pincode);
           add_heading = itemView.findViewById(R.id.address_heading);
           address = itemView.findViewById(R.id.address);
        }
    }

}
