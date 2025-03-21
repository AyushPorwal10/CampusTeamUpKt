package com.example.campus_teamup.notification

import dagger.hilt.android.AndroidEntryPoint
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Inject


interface FCMApiService  {
    @POST("sendNotification")
    suspend fun sendNotification(@Body fcmMessage: FcmMessage)

    @POST("sendTeamJointNotification")
    suspend fun sendTeamJointNotification(@Body fcmMessage: FcmMessage)
}

data class FcmMessage(
    val message: Message
)

data class Message(
    val token: String,
    val notification: Notification,
    val data: Map<String, String>
)

data class Notification(
    val title: String,
    val body: String
)
