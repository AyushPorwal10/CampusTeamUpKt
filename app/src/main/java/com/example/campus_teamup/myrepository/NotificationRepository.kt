package com.example.campus_teamup.myrepository

import android.util.Log
import com.example.campus_teamup.notification.FCMApiService
import com.example.campus_teamup.notification.NotificationData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val fcmApiService: FCMApiService
) {


    suspend fun fetchReceiverFCMToken(userId: String?): String? {
        if (userId.isNullOrEmpty()) {
            Log.e("FCM", "User ID is null or empty")
            return null // Return null instead of an empty string
        }

        return try {
            val snapshot =
                firebaseFirestore.collection("all_user_id").document(userId).get().await()

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

    // sending notification and updating list of users to which sender sent notification

    suspend fun sendNotification(
        notificationData: NotificationData,
        listOfPeopleUserSentRequest: List<String>,
        receiverId: String
    ) {
        coroutineScope {
            launch {
                fcmApiService.sendNotification(notificationData)
                Log.d("FCM", "sending notification in repo concurrent")
            }
            launch {
                updateRequestList(notificationData.senderId, listOfPeopleUserSentRequest)
                Log.d("FCM", "updating notification in repo concurrent")
            }

            // this is to add notification data in receiver data so that receiver can see the notification at one place

            launch {
                firebaseFirestore.collection("all_user_id").document(receiverId)
                    .collection("all_user_details").document("team_invites")
                    .collection("all_invites").add(notificationData)
            }

        }
    }

    suspend fun updateRequestList(senderId: String, listOfPeopleUserSentRequest: List<String>) {

        val data = mapOf("request_send_to" to listOfPeopleUserSentRequest)

        firebaseFirestore.collection("request_send_by").document(senderId).set(data).await()
    }

    suspend fun checkIfAlreadyRequestSent(senderId: String, onComplete: (List<String>) -> Unit) {

        val document =
            firebaseFirestore.collection("request_send_by").document(senderId).get().await()

        // listOfUser that person sent request

        if (document.get("request_send_to") != null) {
            val listOfUsers = document.get("request_send_to") as List<String> ?: emptyList()
            onComplete(listOfUsers)
        } else
            onComplete(emptyList())

    }
}