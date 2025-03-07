package com.example.campus_teamup.myrepository

import com.example.campus_teamup.mydataclass.LastMessage
import com.example.campus_teamup.mydataclass.SendMessage
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {


    suspend fun sendMessage(sendMessage: SendMessage, chatRoomId: String) {
        firestore.collection("chat_rooms").document(chatRoomId).collection("chats").add(sendMessage)
            .await()
        firestore.collection("chat_rooms").document(chatRoomId).set(
            LastMessage(
                sendMessage.message,
                sendMessage.senderId,
                sendMessage.timeStamp,
                chatRoomId
            )
        ).await()
    }
}