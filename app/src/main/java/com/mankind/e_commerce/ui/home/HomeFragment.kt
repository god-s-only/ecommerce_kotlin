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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mankind.e_commerce.R
import com.mankind.e_commerce.adapters.ItemsAdapter
import com.mankind.e_commerce.databinding.FragmentHomeBinding
import com.mankind.e_commerce.viewmodel.ViewModel

class HomeFragment : Fragment() {
    private lateinit var binding:FragmentHomeBinding
    private val locations = arrayOf("USA", "Germany", "Nigeria")
    private lateinit var viewModel:ViewModel
    private lateinit var itemsAdapter: ItemsAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_home,
            container,
            false)
        viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, locations)
        binding.spinner.adapter = arrayAdapter
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        viewModel.getAllShoesProducts(binding.progressBar).observe(requireActivity()){
            itemsAdapter = ItemsAdapter(it, context)
            itemsAdapter.notifyDataSetChanged()
        }
        binding.recyclerView.adapter = itemsAdapter
        return binding.root
    }
}