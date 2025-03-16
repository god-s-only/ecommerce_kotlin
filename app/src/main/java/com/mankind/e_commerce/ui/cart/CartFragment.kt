package com.mankind.e_commerce.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mankind.e_commerce.R
import com.mankind.e_commerce.adapters.CartAdapter
import com.mankind.e_commerce.databinding.FragmentCartBinding
import com.mankind.e_commerce.model.CartProductModel
import com.mankind.e_commerce.viewmodel.ViewModel

class CartFragment : Fragment(){
    private lateinit var binding:FragmentCartBinding
    private lateinit var viewModel: ViewModel
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartListItem: ArrayList<CartProductModel>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_cart,
            container,
            false
            )
        var totalPrice = 0.0
        viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        cartListItem = ArrayList()
        cartAdapter = CartAdapter(cartListItem, requireContext())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = cartAdapter
        viewModel.getAllCartProducts().observe(viewLifecycleOwner){cartList ->
            cartListItem.clear()
            cartListItem.addAll(cartList)
            cartAdapter.notifyDataSetChanged()
            val totalPrice = cartListItem.sumOf { it.productPrice.toString().drop(1).toDoubleOrNull() ?:0.0 }
            binding.totalPrice.text = totalPrice.toString()
            binding.totalItems.text = "Total Items (${cartList.size})"
            binding.totalValue.text = totalPrice.toString()
        }
        binding.btnApply.setOnClickListener {
            Toast.makeText(requireContext(), "No promo code for now", Toast.LENGTH_LONG).show()
            binding.promo.text.clear()
        }
        ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ) = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    FirebaseFirestore.getInstance().collection("Cart Products").document(cartListItem[position].merchantId).collection("Products").document(cartListItem[position].productId).delete()
                }
            }
        ).attachToRecyclerView(binding.recyclerView)
        return binding.root
    }
}