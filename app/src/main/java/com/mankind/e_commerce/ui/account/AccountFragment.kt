package com.mankind.e_commerce.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.mankind.e_commerce.R
import com.mankind.e_commerce.databinding.FragmentAccountBinding
import com.mankind.e_commerce.viewmodel.ViewModel


class AccountFragment : Fragment() {
    private lateinit var binding:FragmentAccountBinding
    private lateinit var viewModel: ViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(
            inflater,
            container,
            false
        )
        viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        viewModel.getUserInformation(FirebaseAuth.getInstance().currentUser?.uid).observe(viewLifecycleOwner){
            binding.fullName.text = it.name
            binding.email.text = FirebaseAuth.getInstance().currentUser?.email
        }
        binding.signOut.setOnClickListener {
            viewModel.signOut(requireContext())
        }
        return binding.root
    }

}