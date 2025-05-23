package com.example.new_campus_teamup.myrepository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.new_campus_teamup.mydataclass.LastMessage
import com.example.new_campus_teamup.mydataclass.SendMessage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    @RequiresApi(Build.VERSION_CODES.O)
     fun sendMessage(sendMessage: SendMessage, chatRoomId: String): Result<Unit> {

        return try {
                firestore.runTransaction { transaction ->
                    val sendMessageDocumentRef =
                        firestore.collection("chat_rooms").document(chatRoomId).collection("chats")
                            .document()

                    val lastMessageDocumentReference = firestore.collection("chat_rooms").document(chatRoomId)

                    transaction.set(sendMessageDocumentRef, sendMessage)

                    transaction.set(lastMessageDocumentReference, LastMessage(
                            sendMessage.message,
                            sendMessage.senderId,
                            sendMessage.timeStamp,
                            chatRoomId)
                    )
                }
            Log.d("Transaction","Transaction Completed")
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    fun fetchChatHistory(chatRoomId: String): Flow<List<SendMessage?>> = callbackFlow {

        val chatReference =
            firestore.collection("chat_rooms").document(chatRoomId).collection("chats")
                .orderBy("timeStamp", Query.Direction.ASCENDING)

        val realTimeChatListener = chatReference.addSnapshotListener { snapshot, error ->

            if (error != null) {
                Log.d("ChatHistory", "repo chat fetching error")
                close(error)
                return@addSnapshotListener
            }

            val chatHistory = snapshot?.documents?.mapNotNull { doc ->
                doc.toObject(SendMessage::class.java)
            }
            if (chatHistory != null) {
                Log.d("ChatHistory", "Fetched chat history ")
                trySend(chatHistory)
            } else {
                Log.d("ChatHistory", "repo Chat history null")
            }
        }
        awaitClose { realTimeChatListener.remove() }
    }
}