package com.example.campus_teamup.helper

import android.util.Log
import android.util.Patterns

object CheckEmptyFields {

    fun checkEmail(
        email : String
    ) : String{

        return if(email.isEmpty())
            "Empty Email"
        else if(!isValidEmail(email))
            return "Invalid Email Format"
        else
            ""
    }

    fun checkName(name : String) : String{
        if(name.isEmpty())
            return "Empty Name"
        return ""
    }
     fun isValidEmail(email: String): Boolean {
         val emailPattern = Regex(".+@.+\\.[a-z]+")
         return emailPattern.matches(email)
     }
}
