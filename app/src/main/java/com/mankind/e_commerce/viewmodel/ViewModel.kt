package com.mankind.e_commerce.viewmodel

import android.content.Context
import android.widget.ProgressBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mankind.e_commerce.model.CartProductModel
import com.mankind.e_commerce.model.ProductModel
import com.mankind.e_commerce.model.Repository

class ViewModel : ViewModel() {
    private var repository:Repository

    init {
        repository = Repository()
    }

    fun createNewUser(
        email:String,
        password:String,
        context:Context,
        name: String,
        phoneNumber: String
    ){
        repository.createNewUser(email, password, context, name, phoneNumber)
    }

    fun signInUser(email:String,
                   password:String,
                   context:Context){
        repository.signInUser(email, password, context)
    }

    fun signOut(){
        repository.signOut()
    }
    fun getAllShoesProducts(progressBar: ProgressBar): LiveData<List<ProductModel>>{
        return repository.getAllShoesProducts(progressBar)
    }
    fun addProductsToCart(cartProductModel: CartProductModel, context: Context){
        repository.addProductsToCart(cartProductModel, context)
    }
}