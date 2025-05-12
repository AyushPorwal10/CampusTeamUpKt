package com.example.new_campus_teamup.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.new_campus_teamup.mydataclass.SendMessage
import com.example.new_campus_teamup.myrepository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _chatHistory = MutableStateFlow<List<SendMessage?>>(emptyList())
    val chatHistory : StateFlow<List<SendMessage?>> get() = _chatHistory.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage


    private fun startOperation(block : suspend  () -> Unit){
        viewModelScope.launch {
            try{
                block()
            }
            catch (e : Exception){
                _errorMessage.value = "An unexpected error occurred"
            }

        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendMessage(sendMessage: SendMessage, chatRoomId: String , onMessageSent : () -> Unit , errorSendingMessage : () -> Unit) {
        Log.d(
            "ChatHistory",
            "Going to send message with 3 data \n currentuserid ${sendMessage.senderId} <-  time ${sendMessage.timeStamp} message ${sendMessage.message}"
        )
        startOperation {
            val isMessageSent = chatRepository.sendMessage(sendMessage, chatRoomId)
            isMessageSent.onSuccess {
                onMessageSent()
            }.onFailure {
                errorSendingMessage()
            }
        }
    }

    fun fetchChatHistory(chatRoomId: String){
        Log.d("ChatHistory","viewmodel chat room id is $chatRoomId <-")
        startOperation {
            Log.d("ChatHistory" , "viewmodel Going to fetch chats")

            chatRepository.fetchChatHistory(chatRoomId).collect{chats->
                _chatHistory.value = chats
            }
        }
    }
}