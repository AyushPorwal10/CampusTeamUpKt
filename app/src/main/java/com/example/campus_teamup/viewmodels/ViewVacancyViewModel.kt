package com.example.campus_teamup.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campus_teamup.helper.TimeAndDate
import com.example.campus_teamup.myrepository.ViewVacancyRepository
import com.example.campus_teamup.notification.FcmMessage
import com.example.campus_teamup.notification.Message
import com.example.campus_teamup.notification.Notification
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
) : ViewModel() {


    val tag = "VacancyNotification"
    private val _teamDetails = MutableStateFlow<List<String>>(emptyList())

    val teamDetails: StateFlow<List<String>> = _teamDetails.asStateFlow()

    val _userIdWithMap = MutableStateFlow<Map<String, String>>(emptyMap())
    val userIdWithMap: StateFlow<Map<String, String>> get() = _userIdWithMap.asStateFlow()

    private val _receiverToken = MutableStateFlow<String>("")
    val receiverToken: StateFlow<String> get() = _receiverToken.asStateFlow()

    private val _listOfTeamsThatUserSendRequest = MutableStateFlow<List<String>>(emptyList())
    val listOfTeamsThatUserSendRequest: StateFlow<List<String>> get() = _listOfTeamsThatUserSendRequest.asStateFlow()

    private val _isRequestSent = MutableStateFlow<Boolean>(false)
    val isRequestSent: StateFlow<Boolean> get() = _isRequestSent.asStateFlow()


    fun fetchTeamDetails(postedBy: String) {
        viewModelScope.launch {
            viewVacancyRepository.fetchTeamDetails(postedBy)
                .catch {
                    Log.e("Team", "No team details found")
                }.collect {
                    Log.d("Testing", "Size of teamUserId in viewmodel is ${it.size}")
                    _teamDetails.value = it
                    fetchMemberImage()
                }
        }
    }

    private fun fetchMemberImage() {
        viewModelScope.launch {

            _userIdWithMap.value = viewVacancyRepository.fetchMemberImage(_teamDetails.value)
            Log.d("Testing", "Size of map in viewmodel is ${userIdWithMap.value.size}")
            Log.d("Userid", "Map size is ${_userIdWithMap.value.size}")
        }
    }


    fun getFcmWhoPostedVacancy(receiverId: String) {

        viewModelScope.launch {
            viewVacancyRepository.getFcmWhoPostedVacancy(receiverId).collect { token ->
                Log.d(tag, "Token in viewmodel is $token")
                _receiverToken.value = token
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendNotification(
        currentUserId: String,
        senderName: String,
        onNotificationSent: () -> Unit,
        onNotificationError : ()-> Unit,
        postedBy: String
    ) {

        viewModelScope.launch {
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
                )
            )


            // this is list of teams that current user sent request till now

            _listOfTeamsThatUserSendRequest.value = _listOfTeamsThatUserSendRequest.value + postedBy

            val fcmMessage = FcmMessage(message)
            viewVacancyRepository.sendNotification(
                fcmMessage, onNotificationSent = {
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
        viewModelScope.launch {
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
    }
}