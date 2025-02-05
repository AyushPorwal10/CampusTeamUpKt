package com.example.campus_teamup.helper

import android.content.Context
import android.widget.Toast

object ToastHelper {

    fun showToast(context : Context , message : String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showMessageForEmptyEmail(context : Context , email : String){
        if (email == "Empty Email")
            ToastHelper.showToast(context, "Please enter email")
    }
    fun showMessageForEmptyName(context : Context , name : String){
           if (name == "Empty Name")
            ToastHelper.showToast(context, "Please enter your name")
    }

}