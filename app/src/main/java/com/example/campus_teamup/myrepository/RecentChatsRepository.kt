package com.example.campus_teamup.myrepository

import com.example.campus_teamup.helper.TimeAndDate
import com.example.campus_teamup.mydataclass.RecentChats
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class RecentChatsRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
) {


    suspend fun fetchRecentChats(phoneNumber: String): Flow<List<RecentChats>> = callbackFlow {

        val chatCollectionReference =
            firebaseFirestore.collection("all_user_id").document(phoneNumber).collection("chats")

       val query =  chatCollectionReference.orderBy("lastMessageSentAt",Query.Direction.DESCENDING)

        val realTimeListener = query.addSnapshotListener{snapshot , error->
            if(error != null){
                close(error)
                return@addSnapshotListener
            }

            val userAllChats = snapshot?.documents
                ?.mapNotNull { singleChat ->
                    singleChat.toObject(RecentChats::class.java)
                } ?: emptyList()
            trySend(userAllChats)
        }

        awaitClose {realTimeListener.remove()}

    }
}

