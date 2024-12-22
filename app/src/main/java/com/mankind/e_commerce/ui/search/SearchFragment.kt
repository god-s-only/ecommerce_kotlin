package com.mankind.e_commerce.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mankind.e_commerce.R
import com.mankind.e_commerce.databinding.FragmentSearchBinding

class SearchFragment : Fragment(){
    lateinit var binding:FragmentSearchBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_search,
            container,
            false
            )
        return binding.root
    }

}