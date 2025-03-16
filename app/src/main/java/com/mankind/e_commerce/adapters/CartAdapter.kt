package com.mankind.e_commerce.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.mankind.e_commerce.databinding.ItemListCartBinding
import com.mankind.e_commerce.model.CartProductModel
import com.mankind.e_commerce.R

class CartAdapter(var cartList: List<CartProductModel>, var context: Context)
    : RecyclerView.Adapter<CartAdapter.ViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder
        = ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_list_cart, parent, false))


    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.binding.productName.text = cartList[position].productName
        holder.binding.productPrice.text = cartList[position].productPrice
        holder.binding.productQuantity.text = cartList[position].productQuantity
        Glide.with(context)
            .load(cartList[position].productImageUrl)
            .centerCrop()
            .placeholder(R.drawable.baseline_shopping_cart_24)
            .into(holder.binding.productImage)

        holder.binding.btnIncrease.setOnClickListener {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if(userId != null){
                val documentReference = FirebaseFirestore.getInstance().collection("Cart Products").document(userId).collection("Products").document(cartList[holder.adapterPosition].productId)
                val updatedQuantity = cartList[position].productQuantity.toInt() + 1
                documentReference.update("productQuantity", updatedQuantity.toString())
            }
        }
    }

    override fun getItemCount(): Int = cartList.size


    inner class ViewHolder(var binding: ItemListCartBinding)
        : RecyclerView.ViewHolder(binding.root)
}