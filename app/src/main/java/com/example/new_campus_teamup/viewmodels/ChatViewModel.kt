package com.example.new_campus_teamup.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.new_campus_teamup.helper.CheckNetworkConnectivity
import com.example.new_campus_teamup.mydataclass.SendMessage
import com.example.new_campus_teamup.myrepository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val networkMonitor: CheckNetworkConnectivity
) : ViewModel() {

    private val _chatHistory = MutableStateFlow<List<SendMessage?>>(emptyList())
    val chatHistory : StateFlow<List<SendMessage?>> get() = _chatHistory.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private fun startOperation(block : suspend  () -> Unit){
        viewModelScope.launch {
            if (!networkMonitor.isConnectedNow()) {
                _errorMessage.value = "No internet connection. Please retry later."
                return@launch
            }
            try {
                 block()
            } catch (toe: TimeoutCancellationException) {
                _errorMessage.value = "Request timed out. Check your connection."
            } catch (e: Exception) {
                Log.e("HomeScreenVM", "Unexpected error", e)
                _errorMessage.value = "Something went wrong. Please try again."
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
        startOperation {

            chatRepository.fetchChatHistory(chatRoomId).collect{chats->
                _chatHistory.value = chats
            }

        }
    }
}