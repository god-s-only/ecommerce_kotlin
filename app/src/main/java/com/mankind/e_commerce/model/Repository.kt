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
    private lateinit var collectionReference:CollectionReference
    private lateinit var documentReference:DocumentReference

    init {
        mAuth = FirebaseAuth.getInstance()
        if(mAuth.currentUser != null){
            documentReference = FirebaseFirestore.getInstance().collection("Users Data").document(mAuth.currentUser!!.uid)
            collectionReference = FirebaseFirestore.getInstance().collection("Products")
        }
    }

    fun createNewUser(email:String,
                      password:String,
                      context:Context,
                      name: String,
                      phoneNumber: String){
        var spinKitLoader = SpinKitLoader(context)
        spinKitLoader.showDialog()
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful){
                addUserInformation(name, context, phoneNumber)
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
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(context, "${it.exception?.message}", Toast.LENGTH_LONG).show()
            }
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
    fun addUserInformation(name: String, context: Context, phoneNumber: String){
        if(documentReference != null){
            documentReference.set(UserData(name = name, phoneNumber = phoneNumber)).addOnSuccessListener {
                Toast.makeText(context, "Account created successfully", Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                Toast.makeText(context, "${it.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun getAllProducts(): MutableLiveData<List<ProductModel>>{
        val mutableLiveData = MutableLiveData<List<ProductModel>>()
        collectionReference.get().addOnSuccessListener {snapshot ->
            val productModel = snapshot.mapNotNull { it.toObject(ProductModel::class.java) }
            mutableLiveData.postValue(productModel)
        }.addOnFailureListener {
            mutableLiveData.postValue(emptyList())
        }
        return mutableLiveData
    }

    fun addProducts(){

    }

}