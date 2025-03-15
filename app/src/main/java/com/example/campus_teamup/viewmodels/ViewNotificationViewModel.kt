package com.example.campus_teamup.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campus_teamup.helper.ChatRoomId
import com.example.campus_teamup.myactivities.UserManager
import com.example.campus_teamup.myrepository.ViewNotificationRepository
import com.example.campus_teamup.viewnotifications.NotificationItems
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ViewNotificationViewModel @Inject constructor(
    private val viewNotificationRepository: ViewNotificationRepository,
    private val userManager: UserManager
) : ViewModel() {


    private lateinit var userId: String

    private val _isChatRoomCreated = MutableStateFlow<Boolean>(false)
    val isChatRoomCreated: StateFlow<Boolean>  = _isChatRoomCreated.asStateFlow()

    private val _teamInviteList = MutableStateFlow<List<NotificationItems.TeamInviteNotification>>(
        emptyList()
    )
    val teamInviteList: StateFlow<List<NotificationItems.TeamInviteNotification>> get() = _teamInviteList.asStateFlow()


    fun fetchUserDataFromDatastore() {
        viewModelScope.launch {
            val userData = userManager.userData.first()
            userId = userData.userId
            Log.d("UserNotification", "User id fetched from datastore is $userId")
        }
    }

    fun createChatRoom(index : Int){
        viewModelScope.launch {
            // receiver means the person who accepts invites
            val senderId = teamInviteList.value[index].senderId
            val senderName = teamInviteList.value[index].senderName
            val chatRoomId = ChatRoomId.getChatRoomId(senderId,userId)

            viewNotificationRepository.createChatRoom(senderName,senderId , userId , chatRoomId , onSuccess = {
                Log.d("ChatRoomId","Chat room is created $chatRoomId <-")
                _isChatRoomCreated.value = true
                // if receiver accepts request than remove this from list of notifications
                denyRequest(index)
            } , onFailure = {
                _isChatRoomCreated.value = false
            })

        }
    }


    fun fetchTeamInviteNotifications() {
        viewModelScope.launch {
            viewNotificationRepository.fetchTeamInviteNotifications(userId)
                .collect { listOfInvites ->
                    Log.d(
                        "UserNotification",
                        "Fetched team invite notifications ${listOfInvites.size}"
                    )

                    _teamInviteList.value = listOfInvites
                }
        }
    }

    fun denyRequest(index: Int) {
        val currentList = _teamInviteList.value
        _teamInviteList.value = _teamInviteList.value.toMutableList().apply {
            if (index in indices) {
                Log.d("UserNotification","Sender id from index is ${currentList[index].senderId}")
                viewModelScope.launch {
                    viewNotificationRepository.denyRequest(
                        currentList[index].teamRequestId,
                        userId,
                        currentList[index].senderId
                    )
                }
                removeAt(index)
            }

        }
    }
}