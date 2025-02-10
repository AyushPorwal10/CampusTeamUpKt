package com.example.campus_teamup.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campus_teamup.ActionCodeSettingsProvider
import com.example.campus_teamup.myactivities.UserManager
import com.example.campus_teamup.myrepository.Repository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel  @Inject constructor(
    private val firebaseAuth : FirebaseAuth,
    private val loginSingUpRepo: Repository,
    private val actionCodeSettingsProvider: ActionCodeSettingsProvider,
    private val userManager: UserManager
) : ViewModel() {

    val _isEmailSent = MutableStateFlow<Boolean>(false)
    val isEmailSent : StateFlow<Boolean> = _isEmailSent


   suspend fun signInWithEmailLink(email : String , onFailure : (Exception) -> Unit){

       val actionCodeSettings = actionCodeSettingsProvider.getActionCodeSettings()

       viewModelScope.launch {
           val isEmailRegistered = loginSingUpRepo.isEmailRegistered(email)
           if(isEmailRegistered){
               firebaseAuth.sendSignInLinkToEmail(email , actionCodeSettings)
               _isEmailSent.value = true
           }
           else{
               Log.d("Login","Email not Registered")
               onFailure(Exception("Email not registered"))
           }
       }
    }

    suspend fun saveUserData(email : String){
        userManager.saveUserData(getUserId(email),"",email,"" , "Login")
    }

}