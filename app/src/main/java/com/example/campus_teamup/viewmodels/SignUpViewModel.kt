package com.example.campus_teamup.viewmodels

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campus_teamup.ActionCodeSettingsProvider
import com.example.campus_teamup.myactivities.UserManager
import com.example.campus_teamup.myrepository.Repository
import com.google.firebase.Firebase
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val loginSingUpRepo: Repository,
    private val actionCodeSettingsProvider: ActionCodeSettingsProvider,
    private val userManager: UserManager

) : ViewModel() {

    private val _isEmailSent = MutableStateFlow<Boolean>(false)

    val isEmailSent : StateFlow<Boolean> = _isEmailSent



     suspend fun signUpWithEmailLink(email: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        try {
            val actionCodeSettings = actionCodeSettingsProvider.getActionCodeSettings()

            viewModelScope.launch {
                val isEmailRegistered = loginSingUpRepo.isEmailRegistered(email)

                if(isEmailRegistered){
                    Log.d("Signup","Email is already registered")
                    onFailure(Exception("Already registered"))
                }
                else{
                    firebaseAuth.sendSignInLinkToEmail(email, actionCodeSettings).await()
                    Log.d("Signup" , "Email Send Success")
                    _isEmailSent.value = true
                    onSuccess()
                }
            }

        } catch (e: Exception) {
            Log.d("Signup" , "Something Went Wrong")
            onFailure(e)
        }
    }

    suspend fun saveUserData(email : String , name : String , collegeName : String){
        viewModelScope.launch {
            // adding signup to confirm that this data will be in datastore for sure
            userManager.saveUserData(getUserId(email) ,name , email , collegeName , "Signup")
        }
    }
}
// function to extract userId from email
fun getUserId(email : String) : String{
    return email.substringBefore("@")
}

