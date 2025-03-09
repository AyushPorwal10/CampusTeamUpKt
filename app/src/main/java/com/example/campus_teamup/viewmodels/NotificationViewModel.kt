package com.example.campus_teamup.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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


    private val _receiverFCMToken = MutableStateFlow<String>("")
    private val receiverFCMToken: StateFlow<String> get() = _receiverFCMToken.asStateFlow()

    private val _senderid = MutableStateFlow<String>("")
    private val senderId: StateFlow<String> get() = _senderid.asStateFlow()

    private val _senderName = MutableStateFlow<String>("")
    private val senderName: StateFlow<String> get() = _senderName.asStateFlow()

    fun fetchSenderId(onComplete : ()-> Unit) {
        viewModelScope.launch {
            val userData = userManager.userData.first()
            _senderid.value = userData.userId
            _senderName.value = userData.userName
            Log.d("FCM", "Sender id fetched ${senderId.value} <-")
            onComplete()
        }
    }

    fun fetchReceiverFCMToken(userId: String , onFcmFetched : () -> Unit) {
        viewModelScope.launch {

            val receiverFCM = notificationRepository.fetchReceiverFCMToken(userId)
            Log.d("FCM", "Receiver FCM Fetched $receiverFCM ttttt")
            if (receiverFCM != null) {
                _receiverFCMToken.value = receiverFCM
            }
            onFcmFetched()
        }
    }

    fun sendNotification(title: String, body: String) {
        viewModelScope.launch {
            try {
                Log.d(
                    "FCM",
                    "FCM Token ${receiverFCMToken.value} title $title body $body useId ${senderId.value} userName ${senderName.value}"
                )
                val notificationData = NotificationData(
                    receiverFCMToken.value,
                    title,
                    body,
                    senderId.value,
                    senderName.value
                )
                Log.d("FCM","Sending notification")
                notificationRepository.sendNotification(notificationData)
            } catch (e: Exception) {
                Log.d("FCM", "$e   in notification viewmodel")
            }

        }
    }

}