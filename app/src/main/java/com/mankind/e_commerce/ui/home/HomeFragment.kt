package com.mankind.e_commerce.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.mankind.e_commerce.R
import com.mankind.e_commerce.activities.NewProductActivity
import com.mankind.e_commerce.adapters.ItemsAdapter
import com.mankind.e_commerce.databinding.FragmentHomeBinding
import com.mankind.e_commerce.viewmodel.ViewModel

class HomeFragment : Fragment() {
    private lateinit var binding:FragmentHomeBinding
    private val locations = arrayOf("USA", "Germany", "Nigeria")
    private lateinit var viewModel:ViewModel
    private lateinit var itemsAdapter: ItemsAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, locations)
        binding.spinner.adapter = arrayAdapter
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        viewModel.getAllProducts(binding.progressBar, binding.shoe.text.toString().trim()).observe(viewLifecycleOwner){
            itemsAdapter = ItemsAdapter(it, requireContext())
            binding.recyclerView.adapter = itemsAdapter
            itemsAdapter.notifyDataSetChanged()
        }

        binding.newButton.setOnClickListener{
            Toast.makeText(requireContext(), "That's all the categories we have for now", Toast.LENGTH_LONG).show()
        }

        binding.newButton.setOnClickListener {
            bottomSheetDialog = BottomSheetDialog(requireContext())
            val view = LayoutInflater.from(requireContext()).inflate(R.layout.bottom_fragment, null)
            view.setBackgroundResource(android.R.color.transparent)
            bottomSheetDialog.setContentView(view)
            bottomSheetDialog.show()
            view.findViewById<MaterialButton>(R.id.proceedBtn).setOnClickListener {
                startActivity(Intent(requireContext(), NewProductActivity::class.java))
                bottomSheetDialog.dismiss()
            }
            view.findViewById<MaterialButton>(R.id.cancelBtn).setOnClickListener {
                bottomSheetDialog.dismiss()
            }
        }
        binding.shirtsLayout.setOnClickListener {
            if(!binding.shirtsLayout.background.equals(R.drawable.category_card_active)){
                binding.shirtsLayout.setBackgroundResource(R.drawable.category_card_active)
                showOtherCategories(binding.progressBar, requireContext(), binding.shirt.text.toString().trim())
            }
        }
        return binding.root
    }
    fun showOtherCategories(progressBar: ProgressBar, context: Context, categoryName: String){
        viewModel.getAllProducts(progressBar, categoryName).observe(viewLifecycleOwner){
            itemsAdapter = ItemsAdapter(it, context)
            binding.recyclerView.adapter = itemsAdapter
            itemsAdapter.notifyDataSetChanged()
        }
    }
}