package com.example.campus_teamup.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campus_teamup.mydataclass.SendMessage
import com.example.campus_teamup.myrepository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel  @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel(){


    fun sendMessage(sendMessage: SendMessage , chatRoomId : String){
        viewModelScope.launch {
            chatRepository.sendMessage(sendMessage , chatRoomId)
        }
    }
}