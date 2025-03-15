package com.example.campus_teamup.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campus_teamup.mydataclass.RecentChats
import com.example.campus_teamup.myrepository.RecentChatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentChatsViewModel @Inject constructor(
    private val recentChatsRepository: RecentChatsRepository
) : ViewModel(){


    private val _areChatsLoading = MutableStateFlow<Boolean>(false)
    val areChatsLoading : StateFlow<Boolean> get() = _areChatsLoading.asStateFlow()

    private val _userAllChats = MutableStateFlow<List<RecentChats>>(emptyList())
    val userAllChats : StateFlow<List<RecentChats>> get() = _userAllChats.asStateFlow()



    fun fetchRecentChats(currentUserId : String){
        _areChatsLoading.value = true
        Log.d("Chats","Current user id is $currentUserId <-")
        viewModelScope.launch {
            recentChatsRepository.fetchRecentChats(currentUserId).collect{listOfChats->
                Log.d("Chats","Fetched Chat list size is ${listOfChats.size} <-")
                _userAllChats.value = listOfChats
                _areChatsLoading.value = false
            }
        }
    }
}