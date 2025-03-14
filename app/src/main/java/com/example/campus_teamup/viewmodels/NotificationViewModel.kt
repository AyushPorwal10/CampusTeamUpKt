package com.example.campus_teamup.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campus_teamup.helper.TimeAndDate
import com.example.campus_teamup.myactivities.UserManager
import com.example.campus_teamup.myrepository.NotificationRepository
import com.example.campus_teamup.notification.NotificationData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val userManager: UserManager
) : ViewModel() {


    private val _receiverFCMToken = MutableStateFlow("")
    private val receiverFCMToken: StateFlow<String> get() = _receiverFCMToken.asStateFlow()
    private val _isRequestAlreadySent = MutableStateFlow(false)

    // this is list of userIds that sender sent request in past
    private val _listOfUserId = MutableStateFlow<List<String>>(emptyList())
    private val listOfUserId : StateFlow<List<String>> get() = _listOfUserId.asStateFlow()


    val isRequestAlreadySend : StateFlow<Boolean>get() = _isRequestAlreadySent.asStateFlow()

    private val _senderid = MutableStateFlow<String>("")
     val senderId: StateFlow<String> get() = _senderid.asStateFlow()

    private val _senderName = MutableStateFlow<String>("")
    private val senderName: StateFlow<String> get() = _senderName.asStateFlow()



    // fetching sender id so that it can be sent with message payload

    fun fetchSenderId() {
        viewModelScope.launch {
            val userData = userManager.userData.first()
            _senderid.value = userData.userId
            _senderName.value = userData.userName
            Log.d("FCM", "Sender id fetched ${senderId.value} <-")
            Log.d("FCM","Sender username is ${senderName.value}")
        }
    }

    // this will be used to send notification

    fun fetchReceiverFCMToken(receiverId: String , onFcmFetched : () -> Unit) {
        viewModelScope.launch {

            val receiverFCM = notificationRepository.fetchReceiverFCMToken(receiverId)
            Log.d("FCM", "Receiver FCM Fetched $receiverFCM ttttt")
            if (receiverFCM != null) {
                _receiverFCMToken.value = receiverFCM
            }
            onFcmFetched()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun sendNotification(title: String, body: String, receiverId: String) {
        viewModelScope.launch {
            try {
                Log.d(
                    "FCM",
                    "FCM Token ${receiverFCMToken.value} title $title body $body useId ${senderId.value} userName ${senderName.value}"
                )
                Log.d("Request","New request added to list")
                _listOfUserId.value = _listOfUserId.value + receiverId
                _isRequestAlreadySent.value = true
                val notificationData = NotificationData(
                    TimeAndDate.getCurrentTime(),
                    receiverFCMToken.value,
                    title,
                    body,
                    senderId.value,
                    senderName.value
                )
                Log.d("FCM","Sending notification")
                notificationRepository.sendNotification(notificationData , _listOfUserId.value , receiverId)

            } catch (e: Exception) {
                Log.d("FCM", "$e   in notification viewmodel")
            }

        }
    }


    fun checkIfAlreadyRequestSent(receiverId: String ){
        viewModelScope.launch {
            val listOfUserId = notificationRepository.checkIfAlreadyRequestSent(senderId.value  , onComplete = {
                _listOfUserId.value = it
                Log.d("Request","list of request sent is ${it.size} <-")
                // if request is already sent than no need to send request again
                if(_listOfUserId.value.contains(receiverId)){
                    Log.d("Request","Receiver id present means request is already sent")
                    _isRequestAlreadySent.value = true
                }
            })
        }


    }

}