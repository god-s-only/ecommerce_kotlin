package com.mankind.e_commerce.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
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
        name: String
    ){
        repository.createNewUser(email, password, context, name)
    }

    fun signInUser(email:String,
                   password:String,
                   context:Context){
        repository.signInUser(email, password, context)
    }

    fun signOut(){
        repository.signOut()
    }
}