package com.example.campus_teamup.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campus_teamup.helper.TimeAndDate
import com.example.campus_teamup.myactivities.UserManager
import com.example.campus_teamup.myinterface.RequestSendingState
import com.example.campus_teamup.myrepository.NotificationRepository
import com.example.campus_teamup.notification.FcmMessage
import com.example.campus_teamup.notification.Message
import com.example.campus_teamup.notification.Notification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val userManager: UserManager
) : ViewModel() , RequestSendingState{


    private val _isRequestSending = MutableStateFlow<Boolean>(false)
    override val isRequestSending : StateFlow<Boolean> get() = _isRequestSending.asStateFlow()


    private val _receiverFCMToken = MutableStateFlow("")
    private val receiverFCMToken: StateFlow<String> get() = _receiverFCMToken.asStateFlow()
    private val _isRequestAlreadySent = MutableStateFlow(false)

    // this is list of userIds that sender sent request in past
    private val _listOfUserId = MutableStateFlow<List<String>>(emptyList())
    private val listOfUserId : StateFlow<List<String>> get() = _listOfUserId.asStateFlow()


    val isRequestAlreadySend : StateFlow<Boolean>get() = _isRequestAlreadySent.asStateFlow()

    private val _isChatRoomAlreadyCreated = MutableStateFlow(false)
    val isChatRoomAlreadyCreated : StateFlow<Boolean> get() = _isChatRoomAlreadyCreated.asStateFlow()


    private val _senderid = MutableStateFlow<String>("")
     val senderId: StateFlow<String> get() = _senderid.asStateFlow()

    private val _senderName = MutableStateFlow<String>("")
    private val senderName: StateFlow<String> get() = _senderName.asStateFlow()


    private val _senderPhoneNumber = MutableStateFlow<String>("")
    private val senderPhoneNumber: StateFlow<String> get() = _senderPhoneNumber.asStateFlow()

    // fetching sender id so that it can be sent with message payload

    fun fetchSenderId() {
        viewModelScope.launch {
            val userData = userManager.userData.first()
            _senderid.value = userData.userId
            _senderName.value = userData.userName
            _senderPhoneNumber.value = userData.phoneNumber
            Log.d("FCM", "Sender id fetched ${senderId.value} <-")
            Log.d("FCM","Sender username is ${senderName.value}")
        }
    }

    // this will be used to send notification
    fun fetchReceiverFCMToken(receiverId: String , onFcmFetched : () -> Unit) {
        viewModelScope.launch {
            _isRequestSending.value = true
            val receiverFCM = notificationRepository.fetchReceiverFCMToken(receiverId)
            Log.d("FCM", "Receiver FCM Fetched $receiverFCM ttttt")
            if (receiverFCM != null) {
                _receiverFCMToken.value = receiverFCM
            }
            onFcmFetched()
        }
    }
@RequiresApi(Build.VERSION_CODES.O)
fun sendNotification(title: String, body: String, receiverId: String, phoneNumber: String) {
    viewModelScope.launch {
        try {
            Log.d("NotificationViewModelRequest","${_isRequestSending.value}")
            Log.d(
                "FCM",
                "FCM Token ${receiverFCMToken.value} title $title body $body useId ${senderId.value} userName ${senderName.value}"
            )
            Log.d("Request", "New request added to list")
            _listOfUserId.value = _listOfUserId.value + receiverId
            _isRequestAlreadySent.value = true

            // Build the payload according to HTTP v1 API requirements:
            val message = Message(
                token = receiverFCMToken.value,
                notification = Notification(
                    title = title,
                    body = body
                ),
                data = mapOf(
                    "senderId" to senderId.value,
                    "senderName" to senderName.value,
                    "time" to TimeAndDate.getCurrentTime(),
                    "phoneNumber" to senderPhoneNumber.value
                )
            )
            val fcmMessage = FcmMessage(message)

            Log.d("FCM", "Sending notification")
            notificationRepository.sendNotification(fcmMessage, _listOfUserId.value, receiverId, phoneNumber)
            _isRequestSending.value = false
            Log.d("NotificationViewModelRequest"," Just after function ${_isRequestSending.value}")
        }
        catch (e: HttpException) {
            Log.e("FCM", "HTTP Exception: ${e.response()?.errorBody()?.string()}")
        }
        catch (e: Exception) {
            Log.d("FCM", "$e in notification viewmodel")
        }
    }
}


    fun checkIfAlreadyRequestSent(receiverId: String ){
//        viewModelScope.launch {
//            notificationRepository.checkIfAlreadyRequestSent(senderId.value  , onComplete = {
//                _listOfUserId.value = it
//                Log.d("Request","list of request sent is ${it.size} <-")
//                // if request is already sent than no need to send request again
//                if(_listOfUserId.value.contains(receiverId)){
//                    Log.d("Request","Receiver id present means request is already sent")
//                    _isRequestAlreadySent.value = true
//                }
//            })
//        }
//        viewModelScope.launch {
//            userManager.userData.collectLatest {
//                Log.d("ChatRoomId","Chat Room is created = ${_isChatRoomAlreadyCreated.value}")
//                Log.d("ChatRoomId","${it.userId} <- sender $receiverId <- receiver")
//                _isChatRoomAlreadyCreated.value = notificationRepository.checkIfChatRoomAlreadyCreated(it.userId , receiverId)
//                Log.d("ChatRoomId","Chat Room is created result in check  = ${_isChatRoomAlreadyCreated.value}")
//            }
//        }

        viewModelScope.launch {
            userManager.userData
                .mapLatest { userData ->
                    val listOfSentRequests = notificationRepository.checkIfAlreadyRequestSent(senderId.value)
                    val isChatRoomCreated = notificationRepository.checkIfChatRoomAlreadyCreated(userData.userId, receiverId)
                    Pair(listOfSentRequests, isChatRoomCreated)
                }
                .collectLatest { (listOfSentRequests, chatRoomCreated) ->
                    _listOfUserId.value = listOfSentRequests
                    Log.d("Request", "list of request sent is ${listOfSentRequests.size} <-")

                    if (listOfSentRequests.contains(receiverId)) {
                        Log.d("Request", "Receiver id present means request is already sent")
                        _isRequestAlreadySent.value = true
                    } else {
                        _isRequestAlreadySent.value = false
                    }

                    _isChatRoomAlreadyCreated.value = chatRoomCreated
                    Log.d("ChatRoomId", "Chat Room is created result in check = $chatRoomCreated")
                }
        }

    }



}