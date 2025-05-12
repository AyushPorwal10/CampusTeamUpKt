package com.example.new_campus_teamup.myrepository

import android.util.Log
import com.example.new_campus_teamup.helper.ChatRoomId
import com.example.new_campus_teamup.notification.FCMApiService
import com.example.new_campus_teamup.notification.FcmMessage
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
                firebaseFirestore.collection("all_fcm").document(userId).get().await()

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



    suspend fun sendNotification(
        fcmMessage: FcmMessage,
        listOfPeopleUserSentRequest: List<String>,
        receiverId: String,
        phoneNumber: String,
    ) {
        val batch = firebaseFirestore.batch()

        try {
            coroutineScope {
                Log.d("FCM", "Fcm in notification repo is ${fcmMessage.message.token}")

                // Launch concurrent operations
                launch {
                   fcmApiService.sendNotification(fcmMessage)

                    Log.d("FCM", "Sent notification successfully")
                }

                launch {
                    val requestListDoc = firebaseFirestore.collection("request_send_by").document(fcmMessage.message.data["senderId"] ?: "")
                    batch.set(requestListDoc, mapOf("role_request_send_to" to listOfPeopleUserSentRequest))
                    Log.d("FCM", "Added request list update to batch")
                }

                launch {
                    val notificationDoc = firebaseFirestore.collection("all_user_id").document(phoneNumber)
                        .collection("team_invites").document()

                    val notificationData = mapOf(
                        "title" to fcmMessage.message.notification.title,
                        "message" to fcmMessage.message.notification.body,
                        "senderId" to fcmMessage.message.data["senderId"],
                        "senderName" to fcmMessage.message.data["senderName"],
                        "time" to fcmMessage.message.data["time"],
                        "senderPhoneNumber" to fcmMessage.message.data["phoneNumber"]
                    )

                    batch.set(notificationDoc, notificationData)
                    Log.d("FCM", "Added notification data to batch")
                }
            }

            batch.commit().await()
            Log.d("FCM", "Batch commit successful")

        } catch (e: Exception) {
            Log.e("FCM", "Error during batch commit: ${e.message}")
        }
    }

    suspend fun checkIfAlreadyRequestSent(senderId: String) : List<String> {
        return try {
            val document = firebaseFirestore
                .collection("request_send_by")
                .document(senderId)
                .get()
                .await()

            if (document.exists()) {
                val listOfUsers = document.get("role_request_send_to") as? List<String>
                listOfUsers ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.d("Request", "Error checking if request already sent: $e")
            emptyList()
        }
    }


    suspend fun checkIfChatRoomAlreadyCreated(currentUserId: String , userWhoPostedRole : String ) : Boolean{

        val chatRoomId = ChatRoomId.getChatRoomId(currentUserId , userWhoPostedRole)
        Log.d("ChatRoomId","Chat room id is $chatRoomId")

        val documentRef = firebaseFirestore.collection("chat_rooms").document(chatRoomId)
        return try{
            val document = documentRef.get().await()
            Log.d("ChatRoomId","${document.exists()}")
            document.exists()
        }
        catch (e : Exception){
            Log.d("ChatRoomId","Error checking chat created or not $e")
            false
        }
    }
}