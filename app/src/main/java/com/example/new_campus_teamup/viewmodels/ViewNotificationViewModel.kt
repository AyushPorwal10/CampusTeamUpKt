package com.example.new_campus_teamup.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.new_campus_teamup.helper.ChatRoomId
import com.example.new_campus_teamup.myrepository.ViewNotificationRepository
import com.example.new_campus_teamup.viewnotifications.NotificationItems
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _isChatRoomCreating = MutableStateFlow<Boolean>(false)
    val isChatRoomCreating : StateFlow<Boolean> get() = _isChatRoomCreating.asStateFlow()

    val teamInviteList: StateFlow<List<NotificationItems.TeamInviteNotification>> get() = _teamInviteList.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage


    private val _combineNotificationList = MutableStateFlow<List<NotificationItems>>(
        emptyList()
    )



    val combineNotificationList: StateFlow<List<NotificationItems>> get() = _combineNotificationList.asStateFlow()

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
    fun createChatRoom(index: Int, currentUserId: String?,currentUserName : String?, phoneNumber: String? , onError : () -> Unit ) {
        startOperation {
            Log.d("ChatRoom","Index of combined list is $index")
            _isChatRoomCreating.value = true

            val teamInviteNotificationAtSpecificIndex = combineNotificationList.value[index]

            if(teamInviteNotificationAtSpecificIndex is NotificationItems.TeamInviteNotification){


                val senderId = teamInviteNotificationAtSpecificIndex.senderId
                val senderName = teamInviteNotificationAtSpecificIndex.senderName
                val chatRoomId = ChatRoomId.getChatRoomId(senderId , currentUserId)
                val senderPhoneNumber = teamInviteNotificationAtSpecificIndex.senderPhoneNumber
                val currentUserPhoneNumber = phoneNumber!!



                currentUserId?.let {

                    Log.d("VacancyRequest","Current user id is $it")

                    viewNotificationRepository.createChatRoom(
                        currentUserName!!,
                        senderPhoneNumber,
                        currentUserPhoneNumber ,
                        senderName,
                        senderId,
                        currentUserId,
                        chatRoomId,
                        onSuccess = {
                            Log.d("ChatRoomId", "Chat room is created $chatRoomId <-")
                            _isChatRoomCreated.value = true

                            _isChatRoomCreating.value = false

                            // if receiver accepts request than remove this from list of notifications

                            denyTeamRequest(index, currentUserId ,phoneNumber)
                        },
                        onFailure = {
                            _isChatRoomCreated.value = false
                            _isChatRoomCreating.value = false
                            onError()
                        })
                }
            }


        }
    }


    // this will when a single user see vacancy and apply for team
    @RequiresApi(Build.VERSION_CODES.O)
    fun userRequestCreateChatRoom(index: Int, currentUserId: String?,currentUserName: String? ,  phoneNumber: String? , onError : () -> Unit ) {
        startOperation {
            // receiver means the person who accepts invites
            Log.d("ChatRoomId","Going to creat chat room id for user request  ")

            _isChatRoomCreating.value = true
            val memberInviteNotification = combineNotificationList.value[index]

            if(memberInviteNotification is NotificationItems.MemberInviteNotification){
                val senderId = memberInviteNotification.senderId
                val senderName = memberInviteNotification.senderName
                val chatRoomId = ChatRoomId.getChatRoomId(senderId, currentUserId)
                val senderPhoneNumber = memberInviteNotification.senderPhoneNumber
                currentUserId?.let {

                    Log.d("VacancyRequest","Current user id is $it")

                    viewNotificationRepository.createChatRoom(
                        currentUserName!!,
                        senderPhoneNumber,
                        phoneNumber!!,
                        senderName,
                        senderId,
                        currentUserId,
                        chatRoomId,
                        onSuccess = {
                            Log.d("ChatRoomId", "Chat room is created $chatRoomId <-")
                            _isChatRoomCreated.value = true
                            _isChatRoomCreating.value = false
                            // if receiver accepts request than remove this from list of notifications
                            denyUserRequest(index, currentUserId, phoneNumber)
                        },
                        onFailure = {
                            _isChatRoomCreated.value = false
                            _isChatRoomCreating.value = false
                            onError()
                        })
                }
            }
            else{
                Log.d("ChatRoomId","Not a member invite found ")
            }


        }
    }



    fun denyTeamRequest(index: Int, currentUserId: String? , phoneNumber : String? ) {
        val currentList = _combineNotificationList.value
        _combineNotificationList.value = _combineNotificationList.value.toMutableList().apply {
            if (index in indices) {
                val teamNotification = currentList[index]
                if (teamNotification is NotificationItems.TeamInviteNotification) {
                    startOperation {
                        if (currentUserId != null) {
                            viewNotificationRepository.denyTeamRequest(
                                teamNotification.teamRequestId,
                                currentUserId,
                                teamNotification.senderId,
                                phoneNumber!!
                            )
                        }
                    }
                }
                removeAt(index)
            }
        }
    }

    // this is when a user denies the request who want to join current user team
    fun denyUserRequest(index: Int, currentUserId: String? , phoneNumber: String?) {
        val currentList = _combineNotificationList.value

        _combineNotificationList.value = _combineNotificationList.value.toMutableList().apply {
            if (index in indices) {


                val memberNotification = currentList[index]

                // identifying that notification is type of memberInvite notification
                if (memberNotification is NotificationItems.MemberInviteNotification) {
                    if (currentUserId != null && phoneNumber != null) {
                        startOperation {
                            viewNotificationRepository.denyUserRequest(
                                memberNotification.senderId,
                                currentUserId, memberNotification.senderId,
                                phoneNumber
                            )
                        }
                    }
                } else {
                    Log.d("DenyRequest", "Index is not memberInviteNotification")
                }

            }
        }
    }





    fun fetchCombinedNotifications(phoneNumber: String?) {

        if (phoneNumber != null) {
            Log.d("ShowNotification", "viewmodel current user id is NOT null")

            startOperation {
                viewNotificationRepository.fetchCombinedNotifications(phoneNumber)
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