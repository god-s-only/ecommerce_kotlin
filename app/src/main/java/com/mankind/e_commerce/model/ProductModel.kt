package com.mankind.e_commerce.model

import android.net.Uri

data class ProductModel(var productName:String,
                        var productBio:String,
                        var productPrice:String,
                        var productUri:Uri,
                        var ratings: String,
                        var productImageUrl:String)