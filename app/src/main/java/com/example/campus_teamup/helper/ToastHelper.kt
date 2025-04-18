package com.example.campus_teamup.helper

import android.content.Context
import android.widget.Toast
import android.os.Handler
import android.os.Looper

object ToastHelper {

    fun showToast(context : Context , message : String){

        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

}