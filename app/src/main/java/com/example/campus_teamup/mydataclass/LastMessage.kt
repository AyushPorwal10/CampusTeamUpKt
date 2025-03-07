package com.example.campus_teamup.mydataclass

data class LastMessage(
    val lastMessageSent: String = "",
    val lastMessageSenderId: String = "",
    val lastMessageTimestamp: String,
    val charRoomId: String = ""
)
