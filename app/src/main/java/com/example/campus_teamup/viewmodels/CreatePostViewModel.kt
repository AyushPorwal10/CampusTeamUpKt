package com.example.campus_teamup.viewmodels

import android.util.Log
import androidx.compose.ui.semantics.Role
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campus_teamup.helper.show
import com.example.campus_teamup.myactivities.UserManager
import com.example.campus_teamup.mydataclass.RoleDetails
import com.example.campus_teamup.myrepository.CreatePostRepository
import com.example.campus_teamup.screens.LoginScreen
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val userManager: UserManager,
    private val createPostRepository: CreatePostRepository
) : ViewModel() {

    private lateinit var userId: String
    val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading : StateFlow<Boolean> = _isLoading


    suspend fun fetchDataFromDataStore() {

        Log.d("PostRole", "Fetching of data from datastore started")
        val userData = userManager.userData.first()
        userId = userData.userId
        Log.d("PostRole", "Updated User Id: $userId")
    }

    fun postRole(role: String, datePosted: String) {

        viewModelScope.launch {
            _isLoading.value = true
            Log.d("PostRole","Going to post role")
            try{
                createPostRepository.postRole(
                    userId, RoleDetails(
                        role,
                        datePosted
                    )
                )
            }
            finally {
                _isLoading.value = false
            }

        }
    }

}