package com.greymatter.smartgold.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.greymatter.smartgold.R;
import com.greymatter.smartgold.model.CartListResponse;
import com.greymatter.smartgold.model.OrderResponse;
import com.greymatter.smartgold.utils.Constants;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter <OrderAdapter.ViewHolder>{
    List<OrderResponse.Datum> datumList;
    Context context;

    public OrderAdapter(List<OrderResponse.Datum> cartList, Context context) {
        this.datumList = cartList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        OrderResponse.Datum cart = datumList.get(position);

        holder.product_name.setText(cart.getName());
        holder.product_price.setText(Constants.RUPEES+cart.getDiscountedPrice());
        holder.quantity.setText(cart.getQuantity());
        if (cart.equals("1")){
            holder.method.setText("Pickup at store");
        }else {
            holder.method.setText("Delivery at Home");
        }
        Glide.with(holder.itemView)
                .load(cart.getImage())
                .fitCenter()
                .into(holder.product_image);

/*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra(Constants.PRODUCT_NAME,cart.getName());
                intent.putExtra(Constants.PRODUCT_ID,cart.getId());
                intent.putExtra(Constants.PRICE,cart.getPrice());
                intent.putExtra(Constants.DESCRIPTION,cart.getDescription());
                intent.putExtra(Constants.IMGURL,cart.getImage());
                intent.putExtra(Constants.DISCOUNT_PRICE,cart.getDiscountedPrice());
                context.startActivity(intent);

            }
        });
*/

    }

    @Override
    public int getItemCount() {
        return datumList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView product_name,product_price,quantity,method;
        ImageView product_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            product_name = itemView.findViewById(R.id.product_name);
            product_image = itemView.findViewById(R.id.product_image);
            product_price = itemView.findViewById(R.id.product_price);
            quantity = itemView.findViewById(R.id.quantity);
            method = itemView.findViewById(R.id.method);


        }
    }

}
