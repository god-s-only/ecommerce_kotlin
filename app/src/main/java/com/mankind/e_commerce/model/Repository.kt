package com.mankind.e_commerce.model

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.mankind.e_commerce.activities.ProfileActivity
import com.mankind.e_commerce.activities.SignInActivity
import com.mankind.e_commerce.spinkitloaderstuff.SpinKitLoader

class Repository {
    private var mAuth:FirebaseAuth
    private lateinit var collectionReference: CollectionReference
    private  var documentReference:DocumentReference

    init {
        mAuth = FirebaseAuth.getInstance()
        documentReference = FirebaseFirestore.getInstance().collection("Users Data").document(mAuth.currentUser!!.uid)
        collectionReference = FirebaseFirestore.getInstance().collection("Products")

    }

    fun createNewUser(email:String,
                      password:String,
                      context:Context){
        var spinKitLoader = SpinKitLoader(context)
        spinKitLoader.showDialog()
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful){
                mAuth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
                    if(it.isSuccessful){
                        spinKitLoader.dismissDialog()
                        Toast.makeText(
                            context,
                            "Please check your email for verification link",
                            Toast.LENGTH_LONG
                        ).show()
                        context.startActivity(Intent(context, SignInActivity::class.java))
                    }
                }?.addOnFailureListener {
                    spinKitLoader.dismissDialog()
                    Toast.makeText(
                        context,
                        "${it.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }.addOnFailureListener {
            spinKitLoader.dismissDialog()
            Toast.makeText(context,
                "${it.message}",
                Toast.LENGTH_LONG).show()
        }
    }
    fun signInUser(email:String, password:String, context: Context){
        var spinKitLoader = SpinKitLoader(context)
        spinKitLoader.showDialog()
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful){
                if(mAuth.currentUser?.isEmailVerified == true){
                    spinKitLoader.dismissDialog()
                    Toast.makeText(context,
                        "Login successful",
                        Toast.LENGTH_LONG
                        ).show()
                    context.startActivity(Intent(context, ProfileActivity::class.java))
                }else{
                    mAuth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
                        spinKitLoader.dismissDialog()
                        if(it.isSuccessful){
                            Toast.makeText(
                                context,
                                "Email is not verified, please check email for verification link",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }?.addOnFailureListener {
                        spinKitLoader.dismissDialog()
                        Toast.makeText(
                            context,
                            "${it.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }.addOnFailureListener {
            spinKitLoader.dismissDialog()
            Toast.makeText(
                context,
                "${it.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    fun signOut(){
        mAuth.signOut()
    }
    fun addUserInformation(){
        
    }

    fun getAllProducts(): MutableLiveData<List<ProductModel>>{
        val liveData = ArrayList<ProductModel>()
        val mutableLiveData = MutableLiveData<List<ProductModel>>()
        collectionReference.get().addOnSuccessListener {
            for(snapshot in it){
                val productModel = snapshot.toObject(ProductModel::class.java)
                liveData.add(productModel)
            }
            mutableLiveData.postValue(liveData)
        }
        return mutableLiveData
    }

    fun addProducts(){

    }

}