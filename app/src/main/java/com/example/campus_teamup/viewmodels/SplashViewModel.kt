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

    // checking if user data is in datastore or not , if not fetch from firestore using userId

    suspend fun checkUserDataInDataStore() {
        Log.d("Signup", "Checking status of data")
        viewModelScope.launch {
            val userData = userManager.userData.first()
            val userId = userData.userId
            val userEmail = userData.email
            val userName = userData.userName
            val collegeName = userData.collegeName
            val loginOrsignUp = userData.loginOrSignUp

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
                repository.saveEmail(userEmail)
                repository.saveUserToCollege(userId , collegeName)

            } else {  // fetch from db using userId
                Log.d("Signup", "User already registered  fetching data from database")

               val userSignUpData = repository.fetchSignUpDetails(userId).toObject(SignupDetails::class.java)

                if(userSignUpData != null){
                    Log.d("Signup","Saving to datastore")
                }
                else{
                    Log.d("Signup","Getting null signup data")
                }
            }
        }

    }


}


