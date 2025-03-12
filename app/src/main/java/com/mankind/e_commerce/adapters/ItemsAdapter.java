package com.mankind.e_commerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mankind.e_commerce.R;
import com.mankind.e_commerce.databinding.ItemListLayoutBinding;
import com.mankind.e_commerce.model.ProductModel;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
    private ArrayList<ProductModel> productModelArrayList;
    private Context context;
    public ItemsAdapter(ArrayList<ProductModel> productModelArrayList, Context context){
        this.productModelArrayList = productModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_list_layout,
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .load(productModelArrayList.get(position).getProductImageUrl())
                .centerCrop()
                .into(holder.itemListLayoutBinding.itemImage);
        holder.itemListLayoutBinding.itemName.setText(productModelArrayList.get(position).getProductName());
        holder.itemListLayoutBinding.itemPrice.setText(productModelArrayList.get(position).getProductPrice());
        holder.itemListLayoutBinding.itemRatings.setText(productModelArrayList.get(position).getRatings());
    }

    @Override
    public int getItemCount() {
        return productModelArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ItemListLayoutBinding itemListLayoutBinding;
        public ViewHolder(ItemListLayoutBinding itemListLayoutBinding){
            super(itemListLayoutBinding.getRoot());
            this.itemListLayoutBinding = itemListLayoutBinding;
        }
    }
}
