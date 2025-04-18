package com.example.campus_teamup.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campus_teamup.myactivities.UserData
import com.example.campus_teamup.mydataclass.FeedbackData
import com.example.campus_teamup.myrepository.UserDataSharedRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserDataSharedViewModel @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val userDataSharedRepository: UserDataSharedRepository
) : ViewModel(){

    val userData : StateFlow<UserData?> = userDataSharedRepository.fetchUserDataFromDataStore()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),null)

    private val _isFeedbackSending  = MutableStateFlow<Boolean>(false)
    val isFeedbackSending : StateFlow<Boolean> get() = _isFeedbackSending.asStateFlow()

    private val _isFeedbackSent = MutableStateFlow<Boolean>(false)
    val isFeedbackSent : StateFlow<Boolean>get() = _isFeedbackSent.asStateFlow()


    fun sendFeedback(feedbackData: FeedbackData , onError : () -> Unit){
        viewModelScope.launch {
            _isFeedbackSending.value  = true
        userData.value?.userId?.let {
            Log.d("Feedback","Going to send feedback for user id $it")
            userDataSharedRepository.sendFeedback(firebaseFirestore , it ,feedbackData , onFeedbackSent = {
                Log.d("Feedback","Feedback sent for user id $it")
                _isFeedbackSending.value = false
                _isFeedbackSent.value = true
            }, onError = {
                _isFeedbackSending.value = false
                onError()
            })
        }
        }
    }

    fun updateFeedbackSentStatus(){
        _isFeedbackSent.value = false
    }
}