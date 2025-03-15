package com.mankind.e_commerce.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.mankind.e_commerce.R
import com.mankind.e_commerce.databinding.FragmentCartBinding

class CartFragment : Fragment(){
    lateinit var binding:FragmentCartBinding
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
        return binding.root
    }

}