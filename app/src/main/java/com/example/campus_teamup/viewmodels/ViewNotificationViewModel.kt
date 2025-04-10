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
) : ViewModel() {


    private val _isChatRoomCreated = MutableStateFlow<Boolean>(false)
    val isChatRoomCreated: StateFlow<Boolean> = _isChatRoomCreated.asStateFlow()

    private val _teamInviteList = MutableStateFlow<List<NotificationItems.TeamInviteNotification>>(
        emptyList()
    )
    val teamInviteList: StateFlow<List<NotificationItems.TeamInviteNotification>> get() = _teamInviteList.asStateFlow()


    private val _combineNotificationList = MutableStateFlow<List<NotificationItems>>(
        emptyList()
    )
    val combineNotificationList: StateFlow<List<NotificationItems>> get() = _combineNotificationList.asStateFlow()


    fun createChatRoom(index: Int, currentUserId: String?) {
        viewModelScope.launch {
            // receiver means the person who accepts invites
            val senderId = teamInviteList.value[index].senderId
            val senderName = teamInviteList.value[index].senderName
            val chatRoomId = ChatRoomId.getChatRoomId(senderId, currentUserId)

            viewNotificationRepository.createChatRoom(
                senderName,
                senderId,
                currentUserId,
                chatRoomId,
                onSuccess = {
                    Log.d("ChatRoomId", "Chat room is created $chatRoomId <-")
                    _isChatRoomCreated.value = true
                    // if receiver accepts request than remove this from list of notifications
                    denyTeamRequest(index, currentUserId)
                },
                onFailure = {
                    _isChatRoomCreated.value = false
                })
        }
    }


    fun denyTeamRequest(index: Int, currentUserId: String?) {
        val currentList = _combineNotificationList.value
        _combineNotificationList.value = _combineNotificationList.value.toMutableList().apply {
            if (index in indices) {
                val teamNotification = currentList[index]
                if (teamNotification is NotificationItems.TeamInviteNotification) {
                    viewModelScope.launch {
                        if (currentUserId != null) {
                            viewNotificationRepository.denyTeamRequest(
                                teamNotification.teamRequestId,
                                currentUserId,
                                teamNotification.senderId
                            )
                        }
                    }
                }
                removeAt(index)
            }
        }
    }

    // this is when a user denies the request who want to join current user team
    fun denyUserRequest(index: Int, currentUserId: String?) {
        val currentList = _combineNotificationList.value

        _combineNotificationList.value = _combineNotificationList.value.toMutableList().apply {
            if (index in indices) {


                val memberNotification = currentList[index]

                // identifying that notification is type of memberInvite notification
                if (memberNotification is NotificationItems.MemberInviteNotification) {
                    if (currentUserId != null) {
                        viewModelScope.launch {
                            viewNotificationRepository.denyUserRequest(
                                memberNotification.senderId,
                                currentUserId, memberNotification.senderId
                            )
                        }
                    }
                } else {
                    Log.d("DenyRequest", "Index is not memberInviteNotification")
                }

            }
        }
    }


//    fun fetchMemberInviteNotifications(currentUserId: String?){
//        if(currentUserId == null)
//            return
//        viewModelScope.launch {
//            viewNotificationRepository.fetchMemberInviteNotifications(currentUserId).collect{list->
//                _memberInviteList.value = list
//            }
//        }
//    }
//


    fun fetchCombinedNotifications(currentUserId: String?) {

        if (currentUserId != null) {
            Log.d("ShowNotification", "viewmodel current user id is NOT null")

            viewModelScope.launch {
                viewNotificationRepository.fetchCombinedNotifications(currentUserId)
                    .collect { allNotification ->
                        Log.d(
                            "ShowNotification",
                            "All notification in viewmodel is ${allNotification.size}"
                        )
                        _combineNotificationList.value = allNotification
                    }
            }
        } else {
            Log.d("ShowNotification", "viewmodel current user id is null")
        }
    }

}