package com.mankind.e_commerce.model

import android.net.Uri

data class UserDataModel(var productName:String,
                         var productBio:String,
                         var productPrice:Int,
                         var productUri:Uri,
                         var productImageUrl:String)