package com.example.campus_teamup.viewnotifications

sealed class NotificationItems {
    data class TeamInviteNotification(
        var teamRequestId : String = "",
        val time: String = "",
        val senderId: String = "",
        val senderName: String = "",
    )
}