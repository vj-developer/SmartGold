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
import com.greymatter.smartgold.model.AddressListResponse;
import com.greymatter.smartgold.model.CategoryResponse;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter <CategoryAdapter.ViewHolder>{
    List<CategoryResponse.Datum> categoryList;
    Context context;
    public CategoryAdapter(List<CategoryResponse.Datum> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
    }
    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        CategoryResponse.Datum category = categoryList.get(position);
        //String add_position = address.getName();
        holder.name.setText(category.getName());
        Glide.with(holder.itemView)
                .load(category.getImage())
                .fitCenter()
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.imageView);

        }
    }
}
