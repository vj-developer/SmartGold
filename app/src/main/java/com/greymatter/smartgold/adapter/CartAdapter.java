package com.greymatter.smartgold.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.greymatter.smartgold.R;
import com.greymatter.smartgold.activity.CartActivity;
import com.greymatter.smartgold.activity.ProductDetailActivity;
import com.greymatter.smartgold.model.AddToCartResponse;
import com.greymatter.smartgold.model.CartListResponse;
import com.greymatter.smartgold.retrofit.APIInterface;
import com.greymatter.smartgold.retrofit.RetrofitBuilder;
import com.greymatter.smartgold.utils.Constants;
import com.greymatter.smartgold.utils.MyFunctions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter <CartAdapter.ViewHolder>{
    List<CartListResponse.Datum> cartList;
    Context context;

    //Callback for quantity change
    OnQuantityChangedListener mCallback;

    public interface OnQuantityChangedListener {
        public void quantityChanged(String product_id,String quantity);
        public void remove(String cart_id);
    }

    public CartAdapter(List<CartListResponse.Datum> cartList, Context context,OnQuantityChangedListener mCallback) {
        this.cartList = cartList;
        this.context = context;
        this.mCallback = mCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CartListResponse.Datum cart = cartList.get(position);

        if (cart.getStatus().equals(Constants.NOT_AVAILABLE)){
            holder.not_available.setVisibility(View.VISIBLE);
        }else {
            holder.not_available.setVisibility(View.GONE);
        }

        holder.product_name.setText(cart.getName());
        holder.product_price.setText(MyFunctions.ConvertToINR(String.valueOf(cart.getDiscountedPrice())));
        holder.quantity.setText(cart.getQuantity());
        Glide.with(holder.itemView)
                .load(cart.getImage())
                .fitCenter()
                .into(holder.product_image);

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_quantity = String.valueOf(Integer.parseInt(cart.getQuantity()) + 1);
                mCallback.quantityChanged(cart.getProduct_id(),new_quantity);
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_quantity = String.valueOf(Integer.parseInt(cart.getQuantity()) - 1);
                if (Integer.parseInt(new_quantity) >= 1){
                    mCallback.quantityChanged(cart.getProduct_id(),new_quantity);
                }
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.remove(cart.getId());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra(Constants.PRODUCT_ID,cart.getProduct_id());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView product_name,product_price,quantity;
        ImageView product_image,plus,minus,remove,not_available;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            product_name = itemView.findViewById(R.id.product_name);
            product_image = itemView.findViewById(R.id.product_image);
            product_price = itemView.findViewById(R.id.product_price);
            quantity = itemView.findViewById(R.id.quantity);
            plus = itemView.findViewById(R.id.plus);
            minus = itemView.findViewById(R.id.minus);
            remove = itemView.findViewById(R.id.remove);
            not_available = itemView.findViewById(R.id.not_available);

        }
    }

}
