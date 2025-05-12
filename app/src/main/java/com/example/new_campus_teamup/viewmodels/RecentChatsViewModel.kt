package com.example.new_campus_teamup.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.new_campus_teamup.myactivities.UserManager
import com.example.new_campus_teamup.mydataclass.RecentChats
import com.example.new_campus_teamup.myrepository.RecentChatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentChatsViewModel @Inject constructor(
    private val recentChatsRepository: RecentChatsRepository,
    private val userManager: UserManager
) : ViewModel(){


    private val _areChatsLoading = MutableStateFlow<Boolean>(false)
    val areChatsLoading : StateFlow<Boolean> get() = _areChatsLoading.asStateFlow()

    private val _userAllChats = MutableStateFlow<List<RecentChats>>(emptyList())
    val userAllChats : StateFlow<List<RecentChats>> get() = _userAllChats.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage



    fun fetchRecentChats(){
        try{
            _areChatsLoading.value = true
            viewModelScope.launch {
                userManager.userData.collectLatest {
                    recentChatsRepository.fetchRecentChats(it.phoneNumber).collect{listOfChats->
                        Log.d("Chats","Fetched Chat list size is ${listOfChats.size} <-")
                        _userAllChats.value = listOfChats
                        _areChatsLoading.value = false
                    }
                }

            }
        }
        catch (e : Exception){
            _errorMessage.value = "An unexpected error occurred"
        }
        finally {
            _areChatsLoading.value = false
        }
    }
    fun clearError() {
        _errorMessage.value = null
    }
}