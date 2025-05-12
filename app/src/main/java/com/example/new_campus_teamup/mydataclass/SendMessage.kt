package com.example.new_campus_teamup.mydataclass

import androidx.annotation.Keep

@Keep
data class SendMessage(
    val senderId: String = "",
    val timeStamp: String = "",
    val message: String = ""
)
