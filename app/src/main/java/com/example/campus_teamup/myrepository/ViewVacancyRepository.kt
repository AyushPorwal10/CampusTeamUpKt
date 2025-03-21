package com.example.campus_teamup.myrepository

import android.util.Log
import com.example.campus_teamup.notification.FCMApiService
import com.example.campus_teamup.notification.FcmMessage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ViewVacancyRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val fcmApiService: FCMApiService
) {

    private val tag = "VacancyNotification"

    fun fetchTeamDetails(userId: String): Flow<List<String>> = callbackFlow {
        Log.d("Team", userId)
        val teamDetailsSnapshot = firebaseFirestore.collection("all_user_id").document(userId)
            .collection("all_user_details").document("teamDetails").get().await()

        val teamReferencePath = teamDetailsSnapshot.getString("teamReference")

        //  only reason it can null if person who posted
        if (teamReferencePath == null) {
            Log.d("Team", "Team referece path is null")
            close(IllegalStateException("teamReference is null"))
            return@callbackFlow
        }
        Log.d("Team", teamReferencePath)

        val realTimeTeamDetailsUpdate =
            firebaseFirestore.document(teamReferencePath).addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val listOfTeamMembers = snapshot?.get("members") as? List<String> ?: emptyList()
                    Log.d("Team", "Size of team in repository is ${listOfTeamMembers.size}")

                    trySend(listOfTeamMembers)
                }

            }
        awaitClose { realTimeTeamDetailsUpdate.remove() }
    }

    suspend fun fetchMemberImage(userIds: List<String>): Map<String, String> {
        val imageMap = mutableMapOf<String, String>()

        return try {
            val tasks = userIds.map { userId ->
                firebaseFirestore.collection("user_images").document(userId).get()
            }
            val results = tasks.map { it.await() }

            for ((index, document) in results.withIndex()) {
                val userId = userIds[index]
                val imageUrl = document.getString("user_image") ?: ""
                imageMap[userId] = imageUrl
            }


            imageMap
        } catch (e: Exception) {
            Log.e("Testing", "Error fetching user images", e)
            userIds.associateWith { "" } // Return map with empty strings on failure
        }
    }


    suspend fun getFcmWhoPostedVacancy(receiverId: String): Flow<String> = callbackFlow {

        Log.d(tag, "Receiver id is $receiverId <-")

        val documentReference = firebaseFirestore.collection("all_user_id").document(receiverId)


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
        fcmMessage: FcmMessage,
        onNotificationSent: () -> Unit,
        onNotificationError: () -> Unit,
        currentUserId: String?,
        postedBy: String,
        requestList: List<String?>
    ) {

        coroutineScope {
            try {

                firebaseFirestore.runTransaction { transaction ->

                    updateRequestList(transaction, currentUserId, requestList)
                    updateNotificationList(transaction, postedBy, fcmMessage)

                }.await()
                fcmApiService.sendNotification(fcmMessage)

                onNotificationSent()
                Log.d(tag, "Notification sent successfully")
            } catch (e: Exception) {
                Log.e(tag, "Error sending notification: ${e.message}")
                onNotificationError()
            }
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
        fcmMessage: FcmMessage
    ) {
        Log.d(tag, "Updating notification list of $requestReceiverId")
        val data = mapOf(
            "title" to fcmMessage.message.notification.title,
            "message" to fcmMessage.message.notification.body,
            "senderId" to fcmMessage.message.data["senderId"],
            "senderName" to fcmMessage.message.data["senderName"],
            "time" to fcmMessage.message.data["time"]
        )
        Log.d(
            tag, "Updating notification list with data ${fcmMessage.message.notification.title} " +
                    fcmMessage.message.notification.body + fcmMessage.message.data["time"]
        )

        val documentReference =
            firebaseFirestore.collection("all_user_id").document(requestReceiverId)
                .collection("team_join_request")
                .document(fcmMessage.message.data["senderId"]!!)

        transaction.set(documentReference, data)

    }
}