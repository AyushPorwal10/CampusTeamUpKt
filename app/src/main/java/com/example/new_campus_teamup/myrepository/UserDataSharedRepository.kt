package com.example.new_campus_teamup.myrepository

import com.example.new_campus_teamup.myactivities.UserData
import com.example.new_campus_teamup.myactivities.UserManager
import com.example.new_campus_teamup.mydataclass.FeedbackData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserDataSharedRepository @Inject constructor(
    private val userManager: UserManager
) {

    fun fetchUserDataFromDataStore() : Flow<UserData> {
        return userManager.userData
    }

    suspend fun sendFeedback(firebaseFirestore: FirebaseFirestore , currentUserId : String  , feedbackData: FeedbackData ,onFeedbackSent : () -> Unit  ,onError : () -> Unit ){
        try {
            firebaseFirestore.collection("feedback").document(currentUserId).set(feedbackData).await()
            onFeedbackSent()
        }
        catch (e : Exception){
            onError()
        }
    }
}