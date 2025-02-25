package com.example.campus_teamup.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campus_teamup.myactivities.UserManager
import com.example.campus_teamup.mydataclass.SignupDetails
import com.example.campus_teamup.myrepository.Repository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val userManager: UserManager,
    private val repository: Repository
) : ViewModel() {

    private lateinit var userId : String
    private lateinit var userEmail : String
    private lateinit var userName : String
    private lateinit var collegeName : String
    private lateinit var loginOrsignUp : String

    val _isAllDataSaved = MutableStateFlow<Boolean>(false)
    val isAllDataSaved : StateFlow<Boolean> = _isAllDataSaved

    suspend fun checkUserDataInDataStore() {
        Log.d("Signup", "Checking status of data")
        viewModelScope.launch {
            val userData = userManager.userData.first()
             userId = userData.userId
             userEmail = userData.email
             userName = userData.userName
             collegeName = userData.collegeName
             loginOrsignUp = userData.loginOrSignUp

            // if it is signup than save it to db
            if (loginOrsignUp == "Signup") {
                Log.d("Signup", "New User")
                repository.saveUserSignUpDetails(
                    userId, SignupDetails(
                        userId,
                        userName,
                        userEmail,
                        collegeName
                    )
                )

            } else {  // fetch from db using userId
                Log.d("Signup", "User already registered  fetching data from database")

               val userSignUpData = repository.fetchSignUpDetails(userId).toObject(SignupDetails::class.java)

                if(userSignUpData != null){
                    _isAllDataSaved.value = true
                    userManager.saveUserData(userSignUpData.userId ,userSignUpData.userName , userSignUpData.userEmail , userSignUpData.collegeName,"Login" )
                    Log.d("Signup","Saving to datastore")
                }
                else{
                    Log.d("Signup","Getting null signup data")
                }
            }
        }

    }

    suspend fun saveEmail( ){
        Log.d("Signup","Going to save user email $userEmail")
        repository.saveEmail(userEmail)
    }

    suspend fun saveUserIdToCollege(){
        Log.d("Signup","Going to save collegeName $collegeName")
        repository.saveUserToCollege(userId , collegeName.lowercase())
        _isAllDataSaved.value = true
        Log.d("Signup","Done with saving email and collegeName")
    }

}


