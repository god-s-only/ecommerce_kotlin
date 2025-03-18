package com.mankind.e_commerce.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.mankind.e_commerce.MainActivity
import com.mankind.e_commerce.R

class LandingActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var documentReference: DocumentReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_landing)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Handler().postDelayed({
            mAuth = FirebaseAuth.getInstance()
            val userId = mAuth.currentUser?.uid
            if(userId != null){
                documentReference = FirebaseFirestore.getInstance().collection("Users").document(userId)
                if(documentReference != null){
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                }else{
                    startActivity(Intent(applicationContext, SignInActivity::class.java))
                }
            }else{
                startActivity(Intent(applicationContext, SignInActivity::class.java))
            }
            finish()
        }, 4000)
        supportActionBar?.hide()
    }
}