package com.mankind.e_commerce.model

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.mankind.e_commerce.MainActivity
import com.mankind.e_commerce.activities.ProfileActivity
import com.mankind.e_commerce.activities.SignInActivity
import com.mankind.e_commerce.spinkitloaderstuff.SpinKitLoader

class Repository {
    private var mAuth:FirebaseAuth
    private lateinit var shoesCollectionReference:CollectionReference
    private lateinit var shirtsCollectionReference:CollectionReference
    private lateinit var pantsCollectionReference:CollectionReference
    private lateinit var bagsCollectionReference:CollectionReference

    init {
        mAuth = FirebaseAuth.getInstance()
        if(mAuth.currentUser != null){
            shoesCollectionReference = FirebaseFirestore.getInstance().collection("Shoes")
            pantsCollectionReference = FirebaseFirestore.getInstance().collection("Pants")
            bagsCollectionReference = FirebaseFirestore.getInstance().collection("Bags")
            shirtsCollectionReference = FirebaseFirestore.getInstance().collection("Shirts")
        }
    }

    fun createNewUser(
        email: String,
        password: String,
        context: Context,
        name: String,
        phoneNumber: String
    ) {
        val spinKitLoader = SpinKitLoader(context)
        spinKitLoader.showDialog()
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = mAuth.currentUser?.uid
                if (userId != null) {
                    val documentReference = FirebaseFirestore.getInstance()
                        .collection("Users")
                        .document(userId)
                    val userData = UserData(name, phoneNumber)
                    documentReference.set(userData)
                        .addOnSuccessListener {
                            mAuth.currentUser?.sendEmailVerification()
                                ?.addOnCompleteListener { emailTask ->
                                    spinKitLoader.dismissDialog()
                                    if (emailTask.isSuccessful) {
                                        Toast.makeText(
                                            context,
                                            "Please check your email for verification link",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        context.startActivity(Intent(context, SignInActivity::class.java))
                                    }
                                }
                                ?.addOnFailureListener {
                                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                                }
                        }
                        .addOnFailureListener {
                            spinKitLoader.dismissDialog()
                            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                        }
                }
            } else {
                spinKitLoader.dismissDialog()
                Toast.makeText(context, "${task.exception?.message}", Toast.LENGTH_LONG).show()
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
                    context.startActivity(Intent(context, MainActivity::class.java))
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

    fun getAllShoesProducts(progressBar: ProgressBar): MutableLiveData<List<ProductModel>>{
        val mutableLiveData = MutableLiveData<List<ProductModel>>()
        progressBar.visibility = View.VISIBLE
        shoesCollectionReference.get().addOnSuccessListener {snapshot ->
            progressBar.visibility = View.GONE
            val productModel = snapshot.mapNotNull {
                it.toObject(ProductModel::class.java)
            }
            mutableLiveData.postValue(productModel)
        }.addOnFailureListener {
            progressBar.visibility = View.GONE
            mutableLiveData.postValue(emptyList())
        }
        return mutableLiveData
    }

    fun getAllShirtsProducts(): MutableLiveData<List<ProductModel>>{
        val mutableLiveData = MutableLiveData<List<ProductModel>>()
        shirtsCollectionReference.get().addOnSuccessListener {snapshot ->
            val productModel = snapshot.mapNotNull { it.toObject(ProductModel::class.java) }
            mutableLiveData.postValue(productModel)
        }.addOnFailureListener {
            mutableLiveData.postValue(emptyList())
        }
        return mutableLiveData
    }

    fun getAllPantsProducts(): MutableLiveData<List<ProductModel>>{
        val mutableLiveData = MutableLiveData<List<ProductModel>>()
        pantsCollectionReference.get().addOnSuccessListener {snapshot ->
            val productModel = snapshot.mapNotNull { it.toObject(ProductModel::class.java) }
            mutableLiveData.postValue(productModel)
        }.addOnFailureListener {
            mutableLiveData.postValue(emptyList())
        }
        return mutableLiveData
    }

    fun getAllBagsProducts(): MutableLiveData<List<ProductModel>>{
        val mutableLiveData = MutableLiveData<List<ProductModel>>()
        bagsCollectionReference.get().addOnSuccessListener {snapshot ->
            val productModel = snapshot.mapNotNull { it.toObject(ProductModel::class.java) }
            mutableLiveData.postValue(productModel)
        }.addOnFailureListener {
            mutableLiveData.postValue(emptyList())
        }
        return mutableLiveData
    }

    fun addProducts(){

    }

    fun addProductsToCart(cartProductModel: CartProductModel, context: Context){
        val spinKitLoader = SpinKitLoader(context)
        spinKitLoader.showDialog()
        val userId = mAuth.currentUser?.uid
        if(userId != null){
            FirebaseFirestore.getInstance().collection("Cart Products").document(userId).collection("Products").document(cartProductModel.productId).set(cartProductModel).addOnSuccessListener {
                spinKitLoader.dismissDialog()
                Toast.makeText(context, "Product added to cart successfully", Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                spinKitLoader.dismissDialog()
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
        }
    }

}