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
import com.google.firebase.firestore.toObject
import com.mankind.e_commerce.MainActivity
import com.mankind.e_commerce.activities.ProfileActivity
import com.mankind.e_commerce.activities.SignInActivity
import com.mankind.e_commerce.spinkitloaderstuff.SpinKitLoader

class Repository {
    private var mAuth:FirebaseAuth

    init {
        mAuth = FirebaseAuth.getInstance()
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

    fun getAllProducts(progressBar: ProgressBar, collectionName: String): MutableLiveData<List<ProductModel>>{
        val mutableLiveData = MutableLiveData<List<ProductModel>>()
        progressBar.visibility = View.VISIBLE
        FirebaseFirestore.getInstance().collection(collectionName).get().addOnSuccessListener {snapshot ->
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
    fun addProducts(productModel: ProductModel, categoryName: String, documentId: String, context: Context){
        FirebaseFirestore.getInstance().collection(categoryName).document(documentId).set(productModel).addOnSuccessListener {
            Toast.makeText(context, "Product added successfully", Toast.LENGTH_LONG).show()
            context.startActivity(Intent(context, MainActivity::class.java));
        }.addOnFailureListener {
            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
        }
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
    fun getAllCartProducts(): MutableLiveData<List<CartProductModel>>{
        val mutableLiveData = MutableLiveData<List<CartProductModel>>()
        val userId = mAuth.currentUser?.uid
        if(userId != null){
            FirebaseFirestore.getInstance().collection("Cart Products").document(userId).collection("Products").addSnapshotListener {
                value, error ->
                if(error != null){
                    return@addSnapshotListener
                }
                mutableLiveData.postValue(
                    value?.documents?.mapNotNull { it.toObject(CartProductModel::class.java) }
                )
            }
        }
        return mutableLiveData
    }
    fun getSelectedProduct(productId: String, collectionName: String, context: Context): MutableLiveData<ProductModel>{
        val mutableLiveData = MutableLiveData<ProductModel>()
        FirebaseFirestore.getInstance().collection(collectionName).document(productId).get().addOnSuccessListener {
            mutableLiveData.postValue(it.toObject(ProductModel::class.java))
        }.addOnFailureListener {
            Toast.makeText(context, "Error fetching product", Toast.LENGTH_LONG).show()
        }
        return mutableLiveData
    }

}