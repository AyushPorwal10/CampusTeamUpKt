package com.example.campus_teamup.myotp

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campus_teamup.myactivities.UserData
import com.example.campus_teamup.myactivities.UserManager
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignUpLoginViewModel @Inject constructor(
    val firebaseAuth: FirebaseAuth,
    val userManager: UserManager,
    val signUpLoginRepo: SignUpLoginRepo,
) : ViewModel() {

    private val _verificationId = MutableStateFlow("")
    val verificationId: StateFlow<String> = _verificationId
    val tag = "OneTimePass"
    private val _isPhoneVerificationSuccess = MutableStateFlow(false)
    val isPhoneVerificationSuccess: StateFlow<Boolean> get() = _isPhoneVerificationSuccess.asStateFlow()


    private val _isOtpSent = MutableStateFlow(false)
    val isOptSent: StateFlow<Boolean> get() = _isOtpSent.asStateFlow()

    private val _isOtpVerifying = MutableStateFlow(false)
    val isOptVerifying: StateFlow<Boolean> get() = _isOtpVerifying.asStateFlow()

    private val _isVerificationInProgress = MutableStateFlow(false)
    val isVerificationInProgress: StateFlow<Boolean> get() = _isVerificationInProgress.asStateFlow()



    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d(tag, "Verification Complete")
            _message.value = "Auto-verification success"
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.d(tag, "Verification Failed ${e.toString()}")

            _message.value = "Verification failed: ${e.localizedMessage}"
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            _verificationId.value = verificationId
            _message.value = "Code sent successfully"
            _isOtpSent.value = true
            Log.d(tag, "Code sent success Complete")

        }
    }


    fun startVerification(phoneNumber: String, activity: Activity) {
        viewModelScope.launch {
            _isVerificationInProgress.value = true
            signUpLoginRepo.startPhoneNumberVerification(
                "+91$phoneNumber",
                activity,
                callbacks,
                onVerificationSuccess = {
                    _isPhoneVerificationSuccess.value = true
                    _isVerificationInProgress.value = false
                    Log.d(tag, "Phone Verification Success")

                },
                onVerificationFailed = {
                    _isVerificationInProgress.value = false
                    Log.d(tag, "Phone Verification Failed")
                })
        }

    }

    fun verifyOtpAndSignIn(code: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val vid = _verificationId.value
        if (vid.isNotEmpty()) {
            _isOtpVerifying.value = true
            val credential = signUpLoginRepo.verifyOTP(vid, code)
            signUpLoginRepo.signInWithPhoneAuthCredential(credential,
                onSuccess = {
                    Log.d(tag, "OTP verified")
                    _isOtpVerifying.value = false
                    onSuccess()
                },
                onError = {
                    _isOtpVerifying.value = false

                    Log.d(tag, "OTP not verified $it")
                    onError(it)
                }
            )
        } else {
            Log.d(tag, "Invalid verification Id ")
            _message.value = "Invalid verification ID"
        }
    }


    fun isEmailOrPhoneNumberRegistered(
        email: String,
        phoneNumber: String,
        isEmailOrPhoneNumberRegistered: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            _isVerificationInProgress.value = true
            val isRegistered = signUpLoginRepo.isEmailOrPhoneNumberRegistered(email, phoneNumber)
            Log.d("OneTimePass","Is Registered == $isRegistered")
            _isVerificationInProgress.value = false
            isEmailOrPhoneNumberRegistered(isRegistered)

        }
    }

    fun saveUserData(
        email: String,
        name: String,
        collegeName: String,
        phoneNumber: String
    ) {
        viewModelScope.launch {
            Log.d(
                tag,
                "Saving user data email is $email \n name is $name , collegeName is $collegeName"
            )
            userManager.saveUserData(
                getUserId(email),
                name,
                email,
                collegeName,
                phoneNumber,
                "Signup"
            )
            Log.d(tag, "User data saved success")
        }
    }


    // when user do sign up save data to data store
    fun saveUserDataToDatabase(phoneNumber: String , onDataSaved : () -> Unit ) {
        viewModelScope.launch {
            userManager.userData.collectLatest {
                Log.d(
                    tag,
                    "Going to save user data phoneNumber is $phoneNumber userid is ${it.userId} \n username is ${it.userName} \n useremail is ${it.email}"
                )
                signUpLoginRepo.saveUserDataToDatabase(it , onUserDataSaved = {
                    onDataSaved()
                })
            }
        }
    }

    // when user login
     fun fetchDataFromDatabase(phoneNumber: String , onUserDataSaved : () -> Unit ){
        viewModelScope.launch {
            Log.d(tag , "Fetching data from firebase with $phoneNumber")
            signUpLoginRepo.fetchDataFromDatabase(phoneNumber , onUserDataFetched = {document ->

                val userData = document.toObject(UserData::class.java)
                Log.d(
                    tag,
                    "Whille Login data fetched userid is ${userData?.userId} \n username is ${userData?.userName} \n useremail is ${userData?.email}"
                )

                if(userData != null){
                    saveUserData(userData.email,userData.userName,userData.collegeName,userData.phoneNumber)
                    onUserDataSaved()
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