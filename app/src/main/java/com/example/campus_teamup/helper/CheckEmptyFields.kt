package com.example.campus_teamup.helper

import android.util.Log
import android.util.Patterns

object CheckEmptyFields {

    fun checkEmail(
        email: String
    ): String {

        return if (email.isEmpty())
            "Empty Email"
        else if (!isValidEmail(email))
            return "Invalid Email Format"
        else
            ""
    }

    fun checkName(name: String): String {
        if (name.isEmpty())
            return "Empty Name"
        return ""
    }

    fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")

        return emailRegex.matches(email)
    }

    fun checkCodingProfiles(listOfProfiles: List<String>): Boolean {
        listOfProfiles.forEach { profile ->
            if (profile.isEmpty() || !profile.contains(".com"))
                return false
        }
        return true
    }


}
