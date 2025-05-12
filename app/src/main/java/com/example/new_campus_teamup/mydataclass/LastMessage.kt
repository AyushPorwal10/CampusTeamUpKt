package com.example.new_campus_teamup.mydataclass

import androidx.annotation.Keep

@Keep
data class LastMessage(
    val lastMessageSent: String = "",
    val lastMessageSenderId: String = "",
    val lastMessageTimestamp: String="",
    val chatRoomId: String = ""
)
