package com.example.campus_teamup.viewnotifications

sealed class NotificationItems {

    // this is when a team leader found user profile interesting and can send request
    data class TeamInviteNotification(
        var teamRequestId : String = "",
        val time: String = "",
        val senderId: String = "",
        val senderName: String = "",
    ) : NotificationItems()

    // this is when a user wants to join team

    data class MemberInviteNotification(
        val memberRequestId : String = "",
        val time : String = "",
        val senderId : String = "",
        val senderName : String = ""
    ) : NotificationItems()
}