package com.example.new_campus_teamup.myrepository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.new_campus_teamup.mydataclass.LastMessage
import com.example.new_campus_teamup.mydataclass.SendMessage
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {


    fun sendMessage(sendMessage: SendMessage, chatRoomId: String): Result<Unit> {
        return try {
            val batch = firestore.batch()

            val sendMessageDocumentRef = firestore.collection("chat_rooms")
                .document(chatRoomId)
                .collection("chats")
                .document()

            val lastMessageDocumentRef = firestore.collection("chat_rooms")
                .document(chatRoomId)

            batch.set(sendMessageDocumentRef, sendMessage)
            batch.set(lastMessageDocumentRef, LastMessage(
                sendMessage.message,
                sendMessage.senderId,
                sendMessage.timeStamp,
                chatRoomId
            ))

            batch.commit()
            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }



    fun fetchChatHistory(chatRoomId: String): Flow<List<SendMessage>> = callbackFlow {
        val chatReference = firestore.collection("chat_rooms")
            .document(chatRoomId)
            .collection("chats")
            .orderBy("timeStamp", Query.Direction.ASCENDING)

        val listenerRegistration = chatReference.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            val allMessages = snapshot?.documents?.mapNotNull { doc ->
                doc.toObject(SendMessage::class.java)
            } ?: emptyList()

            trySend(allMessages)
        }

        awaitClose { listenerRegistration.remove() }
    }


}