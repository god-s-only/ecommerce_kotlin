package com.mankind.e_commerce.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mankind.e_commerce.R
import com.mankind.e_commerce.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding:FragmentHomeBinding
    private val locations = arrayOf("USA", "Germany", "Nigeria")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_home,
            container,
            false)
        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, locations)
        binding.spinner.adapter = arrayAdapter
        return binding.root
    }
}