package com.example.campus_teamup.notification

import dagger.hilt.android.AndroidEntryPoint
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Inject


interface FCMApiService  {

    @POST("sendNotification")
    suspend fun sendNotification(@Body notificationData: NotificationData)

}

data class NotificationData(
    val fcmToken: String,
    val title: String,
    val body: String,
    val userId: String,
    val userName: String,
)