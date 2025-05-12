package com.example.new_campus_teamup.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.new_campus_teamup.helper.TimeAndDate
import com.example.new_campus_teamup.myinterface.RequestSendingState
import com.example.new_campus_teamup.myrepository.ViewVacancyRepository
import com.example.new_campus_teamup.notification.FcmMessage
import com.example.new_campus_teamup.notification.Message
import com.example.new_campus_teamup.notification.Notification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewVacancyViewModel @Inject constructor(
    private val viewVacancyRepository: ViewVacancyRepository,
) : ViewModel() , RequestSendingState{

    private val _isRequestSending = MutableStateFlow<Boolean>(false)
    override val isRequestSending : StateFlow<Boolean> get() = _isRequestSending.asStateFlow()

    private val _isChatRoomAlreadyCreated = MutableStateFlow(false)
    val isChatRoomAlreadyCreated : StateFlow<Boolean> get() = _isChatRoomAlreadyCreated.asStateFlow()

    val tag = "VacancyNotification"
    private val _teamDetails = MutableStateFlow<List<String>>(emptyList())


    private val _receiverToken = MutableStateFlow<String>("")

    private val _listOfTeamsThatUserSendRequest = MutableStateFlow<List<String>>(emptyList())

    private val _isRequestSent = MutableStateFlow<Boolean>(false)
    val isRequestSent: StateFlow<Boolean> get() = _isRequestSent.asStateFlow()

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

    fun getFcmWhoPostedVacancy(receiverId: String) {

        startOperation {
            viewVacancyRepository.getFcmWhoPostedVacancy(receiverId).collect { token ->
                Log.d(tag, "Token in viewmodel is $token")
                _receiverToken.value = token
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendNotification(
        currentUserPhoneNumber : String ,
        currentUserId: String,
        senderName: String,
        onNotificationSent: () -> Unit,
        onNotificationError : ()-> Unit,
        postedBy: String,
        phoneNumberWhoPosted : String
    ) {

        startOperation {
            _isRequestSending.value = true
            val message = Message(
                _receiverToken.value,
                notification = Notification(
                    "Team join Request",
                    "$senderName wants to join your team."
                ),
                data = mapOf(
                    "senderId" to currentUserId,
                    "senderName" to senderName,
                    "time" to TimeAndDate.getCurrentTime(),
                    "phoneNumber" to currentUserPhoneNumber,
                )
            )
            Log.d("VacancyRequest","Sender name is $senderName , Sender phone number is $currentUserPhoneNumber")


            // this is list of teams that current user sent request till now

            _listOfTeamsThatUserSendRequest.value = _listOfTeamsThatUserSendRequest.value + postedBy

            val fcmMessage = FcmMessage(message)
            viewVacancyRepository.sendNotification(
                currentUserPhoneNumber,// this is sender means current user phone number
                phoneNumberWhoPosted,
                fcmMessage, onNotificationSent = {
                    _isRequestSending.value = false
                    onNotificationSent()
                },
                onNotificationError = {
                                      onNotificationError()
                }, currentUserId,
                postedBy,
                _listOfTeamsThatUserSendRequest.value
            )
        }
    }

    fun checkIfRequestAlreadySent(currentUserId: String?, personWhoPostedVacancy: String) {
        if (currentUserId == null)
            return
        startOperation {
            viewVacancyRepository.checkIfRequestAlreadySent(currentUserId)
                .catch {
                    Log.e(tag,"List is null")
                }.collect {
                _listOfTeamsThatUserSendRequest.value = it
                if (_listOfTeamsThatUserSendRequest.value.contains(personWhoPostedVacancy)) {
                    Log.d(tag, "Viewmodel Request is already sent ")
                    _isRequestSent.value = true
                }
                else{
                    _isRequestSent.value = false
                }
            }
        }
        startOperation {
            _isChatRoomAlreadyCreated.value = viewVacancyRepository.checkIfChatRoomAlreadyCreated(currentUserId , personWhoPostedVacancy)
        }
    }
}