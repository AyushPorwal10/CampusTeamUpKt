package com.example.campus_teamup.myrepository

import android.util.Log
import com.example.campus_teamup.mydataclass.SignupDetails
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton


class Repository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) {


    // sign up section
     suspend fun isEmailRegistered(email : String) : Boolean{
        val documentSnapshot = firebaseFirestore.collection("emails").document(email).get().await()
        return documentSnapshot.exists()
    }

    suspend fun saveUserSignUpDetails(userId : String , signupDetails: SignupDetails){
        Log.d("Signup" , "Saving Signup data")
        firebaseFirestore.collection("all_user_id").document(userId).
        collection("all_user_details").document("signup_details").set(signupDetails).await()
    }
    // saving this separate because it becomes easy to check if email is present in db or not
    suspend fun saveEmail(email : String){
        Log.d("Signup","Saving email")
        firebaseFirestore.collection("emails").document(email).set(mapOf("email" to email)).await()
    }

    suspend fun saveUserToCollege(userId: String, collegeName: String) {
        Log.d("Signup","Saving college")
        firebaseFirestore.collection("all_college_india")
            .document(collegeName)
            .update("userIds", FieldValue.arrayUnion(userId)) // Add userId to array
            .await()
    }


    // login section
    suspend fun fetchSignUpDetails(userId: String): DocumentSnapshot {
        Log.d("Signup", "Fetching Signup data")
        return firebaseFirestore.collection("all_user_id").document(userId).get().await()
    }



}