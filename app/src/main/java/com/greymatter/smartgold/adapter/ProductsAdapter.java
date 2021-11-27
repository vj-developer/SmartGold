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
import com.greymatter.smartgold.activity.ProductDetailActivity;
import com.greymatter.smartgold.model.AddressListResponse;
import com.greymatter.smartgold.model.ProductListResponse;
import com.greymatter.smartgold.utils.Constants;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter <ProductsAdapter.ViewHolder>{
    List<ProductListResponse.Datum> productList;
    Context context;

    public ProductsAdapter(List<ProductListResponse.Datum> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductListResponse.Datum products = productList.get(position);
        //String add_position = address.getName();

        holder.pname.setText(products.getName());
        holder.price.setText(Constants.RUPEES+products.getDiscounted_price());
        Glide.with(holder.itemView)
                .load(products.getImage())
                .fitCenter()
                .into(holder.pimage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra(Constants.PRODUCT_NAME,products.getName());
                intent.putExtra(Constants.PRODUCT_ID,products.getId());
                intent.putExtra(Constants.PRICE,products.getPrice());
                intent.putExtra(Constants.DESCRIPTION,products.getDescription());
                intent.putExtra(Constants.IMGURL,products.getImage());
                intent.putExtra(Constants.DISCOUNT_PRICE,products.getDiscounted_price());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView pimage;
        TextView pname,price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           pimage = itemView.findViewById(R.id.imageView);
           pname = itemView.findViewById(R.id.product_name);
           price = itemView.findViewById(R.id.prodct_price);
        }
    }

}
