package com.example.campus_teamup.myrepository

import android.util.Log
import com.example.campus_teamup.mydataclass.CollegeDetails
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserProfileRepo @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) {
    // college Details

    suspend fun saveCollegeDetails(userId : String , collegeDetails: CollegeDetails){
        Log.d("CollegDetails" , "Saving CollegeDetails data")
        firebaseFirestore.collection("all_user_id").document(userId).
        collection("all_user_details").document("college_details").set(collegeDetails).await()
    }
    suspend fun fetchCollegeDetails(userId : String):DocumentSnapshot{
        return firebaseFirestore.collection("all_user_id").document(userId).
        collection("all_user_details").document("college_details").get().await()
    }

    // coding profiles

    suspend fun saveCodingProfiles(userId : String , listOfCodingProfiles : List<String>){
        firebaseFirestore.collection("all_user_id").document(userId)
            .collection("all_user_details").document("coding_profiles").
            set(mapOf("profilelist" to listOfCodingProfiles)).await()
    }
}