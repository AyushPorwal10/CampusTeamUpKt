package com.example.new_campus_teamup.notification

import androidx.annotation.Keep
import retrofit2.http.Body
import retrofit2.http.POST


@Keep
interface FCMApiService  {
    @POST("sendNotification")
    suspend fun sendNotification(@Body fcmMessage: FcmMessage)

    @POST("sendTeamJointNotification")
    suspend fun sendTeamJointNotification(@Body fcmMessage: FcmMessage)
}

@Keep
data class FcmMessage(
    val message: Message
)

@Keep
data class Message(
    val token: String,
    val notification: Notification,
    val data: Map<String, String>
)

@Keep
data class Notification(
    val title: String,
    val body: String
)
