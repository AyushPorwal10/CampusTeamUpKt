package com.example.new_campus_teamup.mydataclass

import androidx.annotation.Keep

@Keep
data class SignupDetails(
    val userId: String = "",
    val userName: String = "",
    val userEmail: String = "",
    val collegeName: String = ""
)
