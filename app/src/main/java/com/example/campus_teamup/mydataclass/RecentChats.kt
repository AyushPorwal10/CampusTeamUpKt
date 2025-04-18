package com.example.campus_teamup.mydataclass

import com.google.firebase.Timestamp

data class RecentChats(
    val createdOn: String = "",
    val chatRoomId: String = "",
    val senderName: String = "",
    val lastMessageSentAt : String = "",
)