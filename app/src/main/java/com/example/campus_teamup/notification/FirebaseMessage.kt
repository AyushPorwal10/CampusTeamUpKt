package com.example.campus_teamup.notification

import android.util.Log
import com.example.campus_teamup.myactivities.UserManager
import com.example.campus_teamup.myrepository.HomeScreenRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FirebaseMessage : FirebaseMessagingService(){

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var homeScreenRepository: HomeScreenRepository

    @Inject
    lateinit var userManager: UserManager


    override fun onNewToken(token: String) {
        super.onNewToken(token)

        // storing it locally and later saving in firestore

        Log.d("FCM","FCM is being $token")
        saveTokenInDataStore(token)

        sendRegistrationToServer(token)
    }

    private fun saveTokenInDataStore(token: String) {

        CoroutineScope(Dispatchers.IO).launch {

            userManager.saveUserFCM(token)
            Log.d("FCM","FCM saved locally $token")

        }
    }

    private fun sendRegistrationToServer(token: String) {
        Log.d("FCM","Going to fetch user id")
        val userId = firebaseAuth.currentUser?.uid ?: return

        CoroutineScope(Dispatchers.IO).launch {
            try {
                homeScreenRepository.saveFcmToken(token, userId)
                Log.d("FCM", "New token saved successfully")
            } catch (e: Exception) {
                Log.e("FCM", "Failed to save new token", e)
            }
        }
    }
}