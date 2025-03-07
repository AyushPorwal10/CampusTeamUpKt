package com.example.campus_teamup.myrepository

import android.util.Log
import androidx.compose.ui.text.toLowerCase
import com.example.campus_teamup.mydataclass.SignupDetails
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class Repository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) {


    // sign up section
    suspend fun isEmailRegistered(email: String): Boolean {
        val documentSnapshot = firebaseFirestore.collection("emails").document(email).get().await()
        return documentSnapshot.exists()
    }

    suspend fun saveUserSignUpDetails(userId: String, signupDetails: SignupDetails) {
        Log.d("Signup", "Saving Signup data")
        firebaseFirestore.collection("all_user_id").document(userId).collection("all_user_details")
            .document("signup_details").set(signupDetails).await()


        // below is separate because to achieve simplicity in team details section
        firebaseFirestore.collection("search_user_id").document(userId).set("userId" to userId)
    }

    // saving this separate because it becomes easy to check if email is present in db or not
    suspend fun saveEmail(email: String) {
        Log.d("Signup", "Saving email")
        firebaseFirestore.collection("emails").document(email).set(mapOf("email" to email)).await()
    }

    suspend fun saveUserToCollege(userId: String, collegeName: String) {
        Log.d("Signup", "Saving college")

        Log.d("Signup", "userId is $userId and collegeName is $collegeName")

        firebaseFirestore.collection("all_college_india").document(collegeName)
            .collection("college_users").document(userId).set(mapOf("userId" to userId)).await()
    }

    // login section
    suspend fun fetchSignUpDetails(userId: String): DocumentSnapshot {
        Log.d("Signup", "Fetching Signup data")
        return firebaseFirestore.collection("all_user_id").document(userId)
            .collection("all_user_details").document("signup_details").get().await()
    }


}