package com.example.campus_teamup.myrepository

import android.util.Log
import com.example.campus_teamup.helper.TimeAndDate
import com.example.campus_teamup.mydataclass.LastMessage
import com.example.campus_teamup.mydataclass.RecentChats
import com.example.campus_teamup.mydataclass.SendMessage
import com.example.campus_teamup.viewnotifications.NotificationItems
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ViewNotificationRepository @Inject constructor(private val firebaseFirestore: FirebaseFirestore) {


    // this are invites from team leader or member because this member founds interest in user profile so they request
    suspend fun fetchTeamInviteNotifications(currentUserId: String): Flow<List<NotificationItems.TeamInviteNotification>> =
        callbackFlow {

            val teamInviteCollection =
                firebaseFirestore.collection("all_user_id").document(currentUserId)
                    .collection("all_invites")

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


    // if user deny request than remove that request from notification section of receiver
    // also update the list of users to which sender sent request means after rejection of request
    // sender can again show interest to let him in team

     fun denyTeamRequest(requestToRemove: String, receiverId: String, senderId: String) {

            // this is take care of atomicity
            val runBatch = firebaseFirestore.batch()

         // this will remove the notification from current user
            val deleteOperation =
                firebaseFirestore.collection("all_user_id").document(receiverId)
                    .collection("all_invites")
                    .document(requestToRemove)

            Log.d(
                "Request",
                "$senderId <-  Going to update sender list of user to whom he send request"
            )


         // purpose is to update the list of users to whom user send request
            val updateOperation = firebaseFirestore.collection("request_send_by").document(senderId)


            runBatch.delete(deleteOperation)
            runBatch.update(updateOperation, "request_send_to" , FieldValue.arrayRemove(receiverId))

            runBatch.commit()

        Log.d("DenyRequest","Team Invite is completed")
    }


    // this is when a user denies the request who want to join current user team
    fun denyUserRequest(requestToRemove: String , receiverId: String , senderId: String){


        val runBath = firebaseFirestore.batch()
        // this operation will remove the notification that receiver receives because receiver decides to deny it
        val removeNotification = firebaseFirestore.collection("all_user_id").document(receiverId).collection("team_join_request")
            .document(requestToRemove)


        // this will remove the userId from sender List to whom sender sends request

        val updateSenderList = firebaseFirestore.collection("team_joint_request").document(senderId)

        runBath.delete(removeNotification)

        runBath.update(updateSenderList , "request_sent_to" , FieldValue.arrayRemove(receiverId))

        runBath.commit()

        Log.d("DenyRequest","Member Invite deny request is complete")
    }

    suspend fun createChatRoom(
        senderName: String,
        senderId: String,
        receiverId: String?,
        chatRoomId: String,
        onSuccess: (Boolean) -> Unit,
        onFailure: (Boolean) -> Unit
    ) {
        Log.d("ChatRoomId", "Repo chat room id $chatRoomId")


        coroutineScope {
            launch {
                firebaseFirestore.collection("chat_rooms").document(chatRoomId).set(
                    LastMessage(
                        "",
                        "",
                        "",
                        chatRoomId
                    )
                ).addOnSuccessListener {
                    onSuccess(true)
                }
                    .addOnFailureListener {
                        onFailure(false)
                    }
            }
            launch {
                firebaseFirestore.collection("chat_rooms").document(chatRoomId).collection("chats")
                    .document().set(
                        SendMessage(
                            "welcome_message",
                            TimeAndDate.getCurrentTime(),
                            "start_chat"
                        )
                    ).await()
            }

            launch {
                firebaseFirestore.collection("all_user_id").document(senderId).collection("chats")
                    .document(chatRoomId).set(
                        RecentChats(
                            TimeAndDate.getCurrentTime(), chatRoomId,
                            senderName, TimeAndDate.getCurrentTime()
                        )
                    )

                if (receiverId != null) {
                    firebaseFirestore.collection("all_user_id").document(receiverId)
                        .collection("chats")
                        .document(chatRoomId).set(
                            RecentChats(
                                TimeAndDate.getCurrentTime(),
                                chatRoomId,
                                senderName,
                                TimeAndDate.getCurrentTime()
                            )
                        )
                }
            }
        }


    }


    // this is when a user wants to join other team
    fun fetchMemberInviteNotifications(currentUserId: String): Flow<List<NotificationItems.MemberInviteNotification>> =
        callbackFlow {


            val collectionReference =
                firebaseFirestore.collection("all_user_id").document(currentUserId)
                    .collection("team_join_request")


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


    suspend fun fetchCombinedNotifications(currentUserId: String): Flow<List<NotificationItems>> {

        val teamInviteNotificationList = fetchTeamInviteNotifications(currentUserId)
        val memberInvitedNotificationList = fetchMemberInviteNotifications(currentUserId)

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

}