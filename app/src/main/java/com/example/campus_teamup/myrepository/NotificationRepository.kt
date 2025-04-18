package com.example.campus_teamup.myrepository

import android.util.Log
import com.example.campus_teamup.helper.ChatRoomId
import com.example.campus_teamup.notification.FCMApiService
import com.example.campus_teamup.notification.FcmMessage
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
        coroutineScope {

            Log.d("FCM","Fcm in notification repo is ${fcmMessage.message.token}")
            launch {
                fcmApiService.sendNotification(fcmMessage)
                Log.d("FCM", "Sent notification successfully")
            }
            launch {
                updateRequestList(fcmMessage.message.data["senderId"] ?: "", listOfPeopleUserSentRequest)
                Log.d("FCM", "Updated request list concurrently")
            }

            launch {
                firebaseFirestore.collection("all_user_id").document(phoneNumber)
                    .collection("team_invites").add(
                        mapOf(
                            "title" to fcmMessage.message.notification.title,
                            "message" to fcmMessage.message.notification.body,
                            "senderId" to fcmMessage.message.data["senderId"],
                            "senderName" to fcmMessage.message.data["senderName"],
                            "time" to fcmMessage.message.data["time"],
                            "senderPhoneNumber" to fcmMessage.message.data["phoneNumber"]
                        )
                    )
                Log.d("FCM", "Saved notification data to Firestore")
            }
        }
    }


    suspend fun updateRequestList(senderId: String, listOfPeopleUserSentRequest: List<String>) {

        val data = mapOf("role_request_send_to" to listOfPeopleUserSentRequest)

        firebaseFirestore.collection("request_send_by").document(senderId).set(data).await()
    }

    suspend fun checkIfAlreadyRequestSent(senderId: String, onComplete: (List<String>) -> Unit) {

        val document =
            firebaseFirestore.collection("request_send_by").document(senderId).get().await()

        // listOfUser that person sent request

        if (document.get("role_request_send_to") != null) {
            val listOfUsers = document.get("role_request_send_to") as List<String> ?: emptyList()
            onComplete(listOfUsers)
        } else
            onComplete(emptyList())

    }


    suspend fun checkIfChatRoomAlreadyCreated(currentUserId: String , userWhoPostedRole : String ) : Boolean{

        val chatRoomId = ChatRoomId.getChatRoomId(currentUserId , userWhoPostedRole)
        val documentRef = firebaseFirestore.collection("chat_rooms").document(chatRoomId)

        return try{
            val document = documentRef.get().await()
            document.exists()
        }
        catch (e : Exception){
            Log.d("ChatRoomId","Error checking chat created or not $e")
            false
        }
    }
}