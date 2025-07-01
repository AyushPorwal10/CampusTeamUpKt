package com.example.new_campus_teamup.myrepository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.new_campus_teamup.helper.TimeAndDate
import com.example.new_campus_teamup.mydataclass.LastMessage
import com.example.new_campus_teamup.mydataclass.RecentChats
import com.example.new_campus_teamup.notification.FCMApiService
import com.example.new_campus_teamup.notification.FcmMessage
import com.example.new_campus_teamup.notification.Message
import com.example.new_campus_teamup.notification.Notification
import com.example.new_campus_teamup.viewnotifications.NotificationItems
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ViewNotificationRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
   val  fcmApiService: FCMApiService) {


    // if user deny request than remove that request from notification section of receiver
    // also update the list of users to which sender sent request means after rejection of request
    // sender can again show interest to let him in team

    fun denyTeamRequest(
        requestToRemove: String,
        receiverId: String,   // currentUser id
        senderId: String,
        phoneNumber: String
    ) {

        // this is take care of atomicity
        val runBatch = firebaseFirestore.batch()

        // this will remove the notification from current user
        val deleteOperation =
            firebaseFirestore.collection("all_user_id").document(receiverId)
                .collection("team_invites")
                .document(requestToRemove)

        Log.d(
            "Request",
            "$senderId <-  Going to update sender list of user to whom he send request"
        )


        // purpose is to update the list of users to whom user send request
        val updateOperation = firebaseFirestore.collection("request_send_by").document(senderId)


        runBatch.delete(deleteOperation)
        runBatch.update(updateOperation, "role_request_send_to", FieldValue.arrayRemove(receiverId))

        runBatch.commit()

        Log.d("DenyRequest", "Team Invite is completed")
    }


    // this is when a user denies the request who want to join current user team
    fun denyUserRequest(
        requestToRemove: String,
        receiverId: String, // currentUser id
        senderId: String,
        phoneNumber: String
    ) {


        val runBath = firebaseFirestore.batch()
        // this operation will remove the notification that receiver receives because receiver decides to deny it
        val removeNotification = firebaseFirestore.collection("all_user_id").document(receiverId)
            .collection("member_invites")
            .document(requestToRemove)


        // this will remove the userId from sender List to whom sender sends request

        val updateSenderList = firebaseFirestore.collection("team_joint_request").document(senderId)

        runBath.delete(removeNotification)

        runBath.update(updateSenderList, "request_sent_to", FieldValue.arrayRemove(receiverId))

        runBath.commit()

        Log.d("DenyRequest", "Member Invite deny request is complete")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun createChatRoom(
        currentUserName: String,
        senderPhoneNumber: String,
        currentUserPhoneNumber: String,
        senderName: String,
        senderId: String,
        receiverId: String,// receiver means current user id
        chatRoomId: String,
        onSuccess: (Boolean) -> Unit,
        onFailure: (Boolean) -> Unit
    ) {


        Log.d(
            "VacancyRequest",
            "Sender name is $senderName senderId is $senderId receiver id(current user id is $receiverId chat rooom id is $chatRoomId",
        )


        Log.d(
            "VacancyRequest",
            "Current user id is $receiverId \n sender id is $senderId"
        )

        coroutineScope {


            try {


                val chatRoomRef = firebaseFirestore.collection("chat_rooms").document(chatRoomId)
                    .collection("chats").document()
                val userChatsRefSender =
                    firebaseFirestore.collection("all_user_id").document(senderId)
                        .collection("chats").document(chatRoomId)

                val batch = firebaseFirestore.batch()

                // Add to chat_rooms
                batch.set(chatRoomRef, LastMessage("", "", chatRoomId))


                // Add to receiver's chats
                val userChatsRefReceiver =
                    firebaseFirestore.collection("all_user_id").document(receiverId)
                        .collection("chats").document(chatRoomId)

                // Add to sender's chats
                batch.set(
                    userChatsRefSender,
                    RecentChats(
                        TimeAndDate.getCurrentTime(),
                        chatRoomId,
                        currentUserName,   // in recent chat list of sender we will find current user name because he accept the request
                        TimeAndDate.getCurrentTime()
                    )
                )

                // this is for current user
                batch.set(
                    userChatsRefReceiver,
                    RecentChats(
                        TimeAndDate.getCurrentTime(),
                        chatRoomId,
                        senderName,
                        TimeAndDate.getCurrentTime()
                    )
                )


                // this will send notification to sender that your request is accepted


                // sender id means the person who send request so show notification to sender that your request is accepted

                sendNotification(currentUserName , senderId)
                batch.commit().await()
                onSuccess(true)
            } catch (e: Exception) {
                Log.e("VacancyRequest", "Failed to create chat room: ${e.message}")
                onFailure(false)
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun sendNotification(currentUserName: String, receiverId: String){

        val receiverFCM = fetchReceiverFCM(receiverId)
        Log.d("FCMTOKEN","Fetched fcm while sending request accept notification")
        val message = Message(
            token = receiverFCM,
            notification = Notification(
                title = "Request accepted by $currentUserName",
                body = "Congratulations , Your request has been accepted \n Now you can send message."
            ),
            data = mapOf(
                "senderId" to "" , // this does not make sense because i just want to show that your request is accepted
                "senderName" to currentUserName,
                "time" to TimeAndDate.getCurrentTime(),
                "phoneNumber" to "123456" // also no need to send this
            )
        )
        val fcmMessage = FcmMessage(message)

        fcmApiService.sendNotification(fcmMessage)

    }

    suspend fun fetchReceiverFCM(receiverId: String  ) : String {

        return suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance()
                .collection("all_fcm")
                .document(receiverId)
                .get()
                .addOnSuccessListener { document ->
                    val fcmToken = document.getString("fcm_token") ?: ""
                    continuation.resume(fcmToken)
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
    }

     fun fetchCombinedNotifications(userId: String): Flow<List<NotificationItems>> {

        val teamInviteNotificationList = fetchTeamInviteNotifications(userId)
        Log.d("ShowNotification", "TeamInvite list size is $teamInviteNotificationList")
        val memberInvitedNotificationList = fetchMemberInviteNotifications(userId)

        return combine(
            teamInviteNotificationList,
            memberInvitedNotificationList
        ) { teamInvite, memberInvite ->
            val combinedList = mutableListOf<NotificationItems>()
            combinedList.addAll(teamInvite)
            combinedList.addAll(memberInvite)
            Log.d("ShowNotification", "Repo all notification size is ${combinedList.size}")
            combinedList
        }


    }


    // this is when a user wants to join other team
    private fun fetchMemberInviteNotifications(userId: String): Flow<List<NotificationItems.MemberInviteNotification>> =
        callbackFlow {


            val collectionReference =
                firebaseFirestore.collection("all_user_id").document(userId)
                    .collection("member_invites")


            val realTimeListener = collectionReference.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }


                val listOfMemberInvites = snapshot?.documents?.mapNotNull { items ->
                    items.toObject(NotificationItems.MemberInviteNotification::class.java)
                } ?: emptyList()

                Log.d(
                    "ShowNotification",
                    "Size of listOfMemberList is ${listOfMemberInvites.size}"
                )
                trySend(listOfMemberInvites)

            }

            awaitClose { realTimeListener.remove() }

        }


    // this are invites from team leader or member because this member founds interest in user profile so they request
    private fun fetchTeamInviteNotifications(userId: String): Flow<List<NotificationItems.TeamInviteNotification>> =
        callbackFlow {
            Log.d("ShowNotification", "Team invite fetching for userId $userId")
            val teamInviteCollection =
                firebaseFirestore.collection("all_user_id").document(userId)
                    .collection("team_invites")

            val listener = teamInviteCollection.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("UserNotification", error.toString())
                    close(error)
                    return@addSnapshotListener
                }
                val listOfNotifications =
                    snapshot?.documents?.mapNotNull { document ->
                        document.toObject(NotificationItems.TeamInviteNotification::class.java)
                            ?.apply {
                                Log.d("UserNotification", "Document id is ${document.id} <-")
                                this.teamRequestId = document.id
                            }
                    } ?: emptyList()

                Log.d("ShowNotification", "Size of listOfTeamInvite is ${listOfNotifications.size}")
                trySend(listOfNotifications)

            }
            awaitClose { listener.remove() }
        }


}