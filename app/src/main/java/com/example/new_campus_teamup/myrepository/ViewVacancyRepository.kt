package com.example.new_campus_teamup.myrepository

import android.util.Log
import com.example.new_campus_teamup.helper.ChatRoomId
import com.example.new_campus_teamup.notification.FCMApiService
import com.example.new_campus_teamup.notification.FcmMessage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ViewVacancyRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val fcmApiService: FCMApiService
) {

    private val tag = "VacancyNotification"
    suspend fun getFcmWhoPostedVacancy(receiverId: String): Flow<String> = callbackFlow {

        Log.d(tag, "Receiver id is $receiverId <-")

        val documentReference = firebaseFirestore.collection("all_fcm").document(receiverId)


        val realTimeTokenListener = documentReference.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.d(tag, "Error in repo ${error.message}")
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val token = snapshot.getString("fcm_token") ?: ""

                trySend(token)

            } else {
                Log.d(tag, "Repo snapshot is null or don't exists")
            }
        }
        awaitClose {
            realTimeTokenListener.remove()
            Log.d(tag, "Listener removed")
        }

    }

    suspend fun sendNotification(
        currentUserPhoneNumber : String ,
        phoneNumberWhoPosted : String ,
        fcmMessage: FcmMessage,
        onNotificationSent: () -> Unit,
        onNotificationError: () -> Unit,
        currentUserId: String?,
        postedBy: String,
        requestList: List<String?>
    ) {

        Log.d(tag , "phoneNumberWhoposted is $phoneNumberWhoPosted fcmMessage all data is " +
                "token is -> ${fcmMessage.message.token} , title is ${fcmMessage.message.notification.title} , body is ${fcmMessage.message.notification.body}" +
                "sender is is ${fcmMessage.message.data["senderId"]} , sender name is ${fcmMessage.message.data["senderName"]}, phoneNumeber is ${fcmMessage.message.data["phoneNumber"]}")


            try {
                firebaseFirestore.runTransaction { transaction ->

                    // list of userid to whom current user send request will be updated

                    updateRequestList(transaction, currentUserId, requestList)

                    // person who posted will find notification section updated
                    updateNotificationList(transaction, postedBy, fcmMessage ,phoneNumberWhoPosted)

                }.await()
                fcmApiService.sendNotification(fcmMessage)

                onNotificationSent()
                Log.d(tag, "Notification sent successfully")
            } catch (e: Exception) {
                Log.e(tag, "Error sending notification: ${e.message}")
                onNotificationError()
            }

    }


    suspend fun checkIfChatRoomAlreadyCreated(currentUserId: String , personWhoPostedVacancy : String ) : Boolean{

        val chatRoomId = ChatRoomId.getChatRoomId(currentUserId , personWhoPostedVacancy)
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
    fun checkIfRequestAlreadySent(currentUserId: String?): Flow<List<String>> = callbackFlow {
        if (currentUserId == null) {
            close(Exception("User_Id_is_null"))
            return@callbackFlow
        }
        val documentReference =
            firebaseFirestore.collection("team_joint_request").document(currentUserId)


        val realTimeRequestChecker = documentReference.addSnapshotListener { snapshot, error ->

            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                var listOfTeamsThatUserSendRequest = snapshot.get("request_sent_to")
                if (listOfTeamsThatUserSendRequest == null)
                    return@addSnapshotListener
                else
                    listOfTeamsThatUserSendRequest = listOfTeamsThatUserSendRequest as List<String>

                Log.d(
                    tag,
                    "listOfTeamsThatUserSendRequest size is ${listOfTeamsThatUserSendRequest.size}"
                )
                trySend(listOfTeamsThatUserSendRequest)
            } else {
                Log.d(tag, "Snapshot is null or not exists")
            }
        }
        awaitClose { realTimeRequestChecker.remove() }

    }

    fun updateRequestList(
        transaction: Transaction,
        currentUserId: String?,
        requestList: List<String?>
    ) {
        if (currentUserId == null)
            return

        val data = mapOf("request_sent_to" to requestList)
        val documentReference =
            firebaseFirestore.collection("team_joint_request").document(currentUserId)

        transaction.set(documentReference, data)
    }

    private fun updateNotificationList(
        transaction: Transaction,
        requestReceiverId: String,
        fcmMessage: FcmMessage,
        phoneNumberWhoPosted: String,
    ) {
        Log.d(tag, "Updating notification list of $requestReceiverId")
        val data = mapOf(
            "title" to fcmMessage.message.notification.title,
            "message" to fcmMessage.message.notification.body,
            "senderId" to fcmMessage.message.data["senderId"],
            "senderName" to fcmMessage.message.data["senderName"],
            "time" to fcmMessage.message.data["time"],
            "senderPhoneNumber" to fcmMessage.message.data["phoneNumber"]
        )
        Log.d(
            tag, "Updating notification list with data ${fcmMessage.message.notification.title} " +
                    fcmMessage.message.notification.body + fcmMessage.message.data["time"]
        )
        Log.d("ShowNotification","Receiver id is $requestReceiverId <-")
        val documentReference =
            firebaseFirestore.collection("all_user_id").document(phoneNumberWhoPosted)
                .collection("member_invites")
                .document(fcmMessage.message.data["senderId"]!!)

        transaction.set(documentReference, data)

    }
}