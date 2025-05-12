package com.example.new_campus_teamup.mydataclass

import androidx.annotation.Keep

@Keep
data class RecentChats(
    val createdOn: String = "",
    val chatRoomId: String = "",
    val senderName: String = "",
    val lastMessageSentAt : String = "",
)