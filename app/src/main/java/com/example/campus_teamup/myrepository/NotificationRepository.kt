package com.example.campus_teamup.myrepository

import android.util.Log
import com.example.campus_teamup.notification.FCMApiService
import com.example.campus_teamup.notification.NotificationData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val fcmApiService: FCMApiService
){


    suspend fun fetchReceiverFCMToken(userId : String?) : String? {
        if (userId.isNullOrEmpty()) {
            Log.e("FCM", "User ID is null or empty")
            return null // Return null instead of an empty string
        }

        return try {
            val snapshot = firebaseFirestore.collection("all_user_id").document(userId).get().await()

            if (snapshot.exists()) {
                val token = snapshot.getString("fcm_token")
                if (token.isNullOrEmpty()) {
                    Log.e("FCM", "FCM token is missing for user: $userId")
                    null
                } else {
                    token
                }
            } else {
                Log.e("FCM", "Document does not exist for user: $userId")
                null
            }
        } catch (e: Exception) {
            Log.e("FCM", "Error fetching FCM token: ${e.message}", e)
            null
        }
    }
    suspend fun sendNotification(notificationData: NotificationData){
        Log.d("FCM","Sending notification in repo with data ${notificationData.body} ${notificationData.fcmToken} ${notificationData.userName}")
        fcmApiService.sendNotification(notificationData)
    }
}