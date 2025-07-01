package com.example.new_campus_teamup.myrepository

import com.example.new_campus_teamup.mydataclass.RecentChats
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class RecentChatsRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
) {


     fun fetchRecentChats(currentUserId: String): Flow<List<RecentChats>> = callbackFlow {

        val chatCollectionReference =
            firebaseFirestore.collection("all_user_id").document(currentUserId).collection("chats")

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

