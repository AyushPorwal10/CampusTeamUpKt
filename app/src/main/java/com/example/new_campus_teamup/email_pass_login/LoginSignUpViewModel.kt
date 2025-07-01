package com.example.new_campus_teamup.email_pass_login

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.new_campus_teamup.helper.CheckNetworkConnectivity
import com.example.new_campus_teamup.myactivities.UserData
import com.example.new_campus_teamup.myactivities.UserManager
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject


@HiltViewModel
class LoginSignUpViewModel @Inject constructor(
    val firebaseAuth: FirebaseAuth,
    val userManager: UserManager,
    val loginSignUpRepo: LoginSignUpRepo,
    private val networkMonitor: CheckNetworkConnectivity
) : ViewModel() {


    val tag = "UserSignUp"


    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage


    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun startOperation(block : suspend () -> Unit ){
        viewModelScope.launch {
            if (!networkMonitor.isConnectedNow()) {
                _errorMessage.value = "No internet connection. Please retry later."
                return@launch
            }
            try {
                _isLoading.value = true
                withTimeout(15_000) { block() }
            } catch (toe: TimeoutCancellationException) {
                _errorMessage.value = "Request timed out. Check your connection."
            } catch (e: Exception) {
                Log.e("HomeScreenVM", "Unexpected error", e)
                _errorMessage.value = "Something went wrong. Please try again."
            }
            finally {
                _isLoading.value = false
                Log.d(tag, "Is Loading value is ${isLoading.value}")
            }
        }
    }

    fun signInUser(email: String, password: String , onSuccess: () -> Unit){
        startOperation {
            val result = loginSignUpRepo.signInUser(email , password)
            if(result.isSuccess){
                onSuccess()
            }
            else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Unknown Error"
            }
        }
    }

    fun signUpUser(email: String, password: String , onSuccess : () -> Unit) {
        startOperation {
            val result = loginSignUpRepo.signUpUser(email , password)

            if(result.isSuccess){
                onSuccess()
            }
            else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Unknown Error"
            }
        }
    }
    fun isEmailRegistered(email : String , isRegistered : (Boolean) -> Unit){
        startOperation {
            val result = loginSignUpRepo.isEmailRegistered(email)
            if(result.isSuccess){
                isRegistered(true)
            }
            else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Unknown error"
                isRegistered(false)
            }
        }
    }

    fun sendPasswordResetEmail(email : String , isEmailSent : (Boolean) -> Unit ){
        startOperation {
            val result = loginSignUpRepo.sendPasswordVerificationEmail(email)

            if(result.isSuccess){
                isEmailSent(true)
            }
            else {
                isEmailSent(false)
            }
        }
    }
    fun resetErrorMessage(){
        _errorMessage.value = null
    }
    fun saveUserData(
        email: String,
        name: String,
        collegeName: String,
        isLoginOrSignUp : String,
        onUserDataSaved: () -> Unit
    ) {

        val userData = UserData(getUserId(email) , name , email , collegeName , isLoginOrSignUp , "123456")

        startOperation {
            coroutineScope {
                val saveToDataStore = launch {
                    Log.d(
                        tag,
                        "Saving user data email is $email \n name is $name , collegeName is $collegeName"
                    )
                    userManager.saveUserData(
                        getUserId(email),
                        name,
                        email,
                        collegeName,
                        "0123456789",
                        "Signup"
                    )
                    Log.d(tag, "User data saved success")
                }

                val saveToFirebase = launch {
                    loginSignUpRepo.saveUserDataToDatabase(userData , onUserDataSaved = {
                        Log.d(tag , "User data saved to firebase")
                    })

                }

                joinAll(saveToDataStore , saveToFirebase)
                onUserDataSaved()
            }
        }
    }

    // when user login
    fun fetchDataFromDatabase(userId: String , onUserDataSaved : () -> Unit ){
        startOperation {
            Log.d(tag , "Fetching data from firebase with $userId")
            loginSignUpRepo.fetchDataFromDatabase(userId , onUserDataFetched = {document ->

                val userData = document.toObject(UserData::class.java)
                Log.d(
                    tag,
                    "While Login data fetched userid is ${userData?.userId} \n username is ${userData?.userName} \n useremail is ${userData?.email}"
                )

                if(userData != null){
                    saveUserData(userData.email,userData.userName,userData.collegeName,userData.phoneNumber, onUserDataSaved = {
                        onUserDataSaved()
                    })

                }
                else {
                    Log.d(tag , "Fetched user data is null")
                    onUserDataSaved()
                }
            })
        }

    }
    fun getUserId(email : String) : String{
        return email.substringBefore("@")
    }
}