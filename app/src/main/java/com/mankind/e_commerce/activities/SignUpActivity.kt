package com.mankind.e_commerce.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.mankind.e_commerce.R
import com.mankind.e_commerce.databinding.ActivitySignInBinding
import com.mankind.e_commerce.databinding.ActivitySignUpBinding
import com.mankind.e_commerce.viewmodel.ViewModel

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySignUpBinding
    private lateinit var viewModel: ViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.hide()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        binding.btnSignUp.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val name = binding.etName.text.toString().trim()
            val phoneNumber = binding.etPhone.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val cPassword = binding.etConfirmPassword.text.toString().trim()
            if(email.isNotEmpty() && name.isNotEmpty() && phoneNumber.isNotEmpty() && password.isNotEmpty() && cPassword.isNotEmpty()){
                if(cPassword.equals(password)){
                    viewModel.createNewUser(email, password, this@SignUpActivity, name, phoneNumber)
                }else{
                    Toast.makeText(applicationContext, "Password does not match", Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(applicationContext, "Please fill in the above fields", Toast.LENGTH_LONG).show()
            }
        }
        binding.tvSignIn.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
        }
    }
}