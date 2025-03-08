package com.example.campus_teamup.myrepository

import com.example.campus_teamup.notification.FCMApiService
import com.example.campus_teamup.notification.NotificationData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val fcmApiService: FCMApiService
){


    suspend fun fetchReceiverFCMToken(userId : String?) : String {
        if(userId != null){
            val snapshot =  firebaseFirestore.collection("all_user_id").document(userId).get().await()

            return snapshot.getString("fcm_token") ?: ""
        }
        return ""
    }
    suspend fun sendNotification(notificationData: NotificationData){
        fcmApiService.sendNotification(notificationData)
    }
}