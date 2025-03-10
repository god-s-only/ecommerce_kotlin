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
import com.mankind.e_commerce.viewmodel.ViewModel

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var viewModel: ViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.hide()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
        viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            if(email.isNotEmpty() && password.isNotEmpty()){
                viewModel.signInUser(email, password, this@SignInActivity)
            }else{
                Toast.makeText(applicationContext, "Please fill in the above fields", Toast.LENGTH_LONG).show()
            }
        }
        binding.redirectSignUp.setOnClickListener {
            startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))
        }
    }
}