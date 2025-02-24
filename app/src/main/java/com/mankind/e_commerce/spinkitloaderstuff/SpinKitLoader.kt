package com.mankind.e_commerce.spinkitloaderstuff

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.appcompat.app.AlertDialog
import com.mankind.e_commerce.R

class SpinKitLoader(var context:Context) {
    private val dialog = Dialog(context)
    private val view = LayoutInflater.from(context).inflate(
        R.layout.spinkit,
        null
    )
    init {
        dialog.setContentView(view)
    }

    fun showDialog(){
        dialog.show()
    }
    fun dismissDialog(){
        dialog.dismiss()
    }
}