package com.mankind.e_commerce.viewmodel

import android.content.Context
import android.widget.ProgressBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mankind.e_commerce.model.CartProductModel
import com.mankind.e_commerce.model.ChatNamesModel
import com.mankind.e_commerce.model.ProductModel
import com.mankind.e_commerce.model.Repository
import com.mankind.e_commerce.model.UserData

class ViewModel : ViewModel() {
    private var repository:Repository = Repository()

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

    fun signOut(context: Context){
        repository.signOut(context)
    }
    fun getAllProducts(progressBar: ProgressBar, collectionName: String): LiveData<List<ProductModel>>{
        return repository.getAllProducts(progressBar, collectionName)
    }
    fun addProductsToCart(cartProductModel: CartProductModel, context: Context){
        repository.addProductsToCart(cartProductModel, context)
    }
    fun getAllCartProducts(): MutableLiveData<List<CartProductModel>>{
        return repository.getAllCartProducts()
    }
    fun getSelectedProduct(productId: String, collectionName: String, context: Context): LiveData<ProductModel>{
        return repository.getSelectedProduct(productId, collectionName, context)
    }
    fun addProducts(productModel: ProductModel, categoryName: String, documentId: String, context: Context){
        repository.addProducts(productModel, categoryName, documentId, context)
    }
    fun getUserInformation(userId: String?): LiveData<UserData>
        = repository.getUserInformation(userId)
    fun getChatUsers(userId: String): LiveData<List<ChatNamesModel>>{
        return getChatUsers(userId)
    }
    fun addChatUsers(userId: String, chattingPartner: String){
        repository.addChatUsers(userId, chattingPartner)
    }
}