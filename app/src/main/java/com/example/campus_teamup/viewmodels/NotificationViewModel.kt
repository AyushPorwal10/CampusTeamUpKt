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
) : ViewModel(){


    private val _receiverFCMToken = MutableStateFlow<String>("")
    private val receiverFCMToken : StateFlow<String> get() = _receiverFCMToken.asStateFlow()

    private val _senderid = MutableStateFlow<String>("")
    private val senderId : StateFlow<String> get() = _senderid.asStateFlow()

    private val _senderName = MutableStateFlow<String>("")
    private val senderName : StateFlow<String>get () = _senderName.asStateFlow()

    fun fetchSenderId(){
        viewModelScope.launch {
            val userData = userManager.userData.first()
            Log.d("Notification","Sender id fetched")
            _senderid.value = userData.userId
            _senderName.value = userData.userName
        }
    }
    fun fetchReceiverFCMToken(userId: String){
        viewModelScope.launch {

          val receiverFCM =   notificationRepository.fetchReceiverFCMToken(userId)
            Log.d("Notification","Receiver FCM Fetched $receiverFCM")
            _receiverFCMToken.value = receiverFCM
        }
    }
    fun sendNotification(title : String , body : String ){
        viewModelScope.launch {
            try{
                Log.d("Notification","FCM Token ${receiverFCMToken.value} title $title body $body useId ${senderId.value} userName ${senderName.value}")
                val notificationData = NotificationData(receiverFCMToken.value ,title,body,senderId.value,senderName.value)
                notificationRepository.sendNotification(notificationData)
            }
            catch (e : Exception){
                Log.d("Notification", e.toString())
            }

        }
    }

}