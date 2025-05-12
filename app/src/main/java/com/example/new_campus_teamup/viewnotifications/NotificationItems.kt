package com.example.new_campus_teamup.viewnotifications

import androidx.annotation.Keep

@Keep
sealed class NotificationItems {

    // this is when a team leader found user profile interesting and can send request

    @Keep
    data class TeamInviteNotification(
        var teamRequestId : String = "",
        val time: String = "",
        val senderId: String = "",
        val senderName: String = "",
        val senderPhoneNumber : String = "",
    ) : NotificationItems()

    // this is when a user wants to join team

    @Keep
    data class MemberInviteNotification(
        val time : String = "",
        val senderId : String = "",
        val senderName : String = "",
        val senderPhoneNumber : String = "",
    ) : NotificationItems()
}