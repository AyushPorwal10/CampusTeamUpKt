package com.example.campus_teamup.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campus_teamup.myactivities.UserData
import com.example.campus_teamup.myrepository.UserDataSharedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class UserDataSharedViewModel @Inject constructor(
    private val userDataSharedRepository: UserDataSharedRepository
) : ViewModel(){

    val userData : StateFlow<UserData?> = userDataSharedRepository.fetchUserDataFromDataStore()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),null)
}