package com.example.campus_teamup.myrepository

import android.net.Uri
import android.util.Log
import com.example.campus_teamup.mydataclass.CollegeDetails
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserProfileRepo @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val storageReference: StorageReference
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

    suspend fun uploadUserImageToStorage(userId: String , imageUri : Uri) : String?{
        return try {

            val imageRef = storageReference.child("user_images/$userId/${System.currentTimeMillis()}.jpg")

            imageRef.putFile(imageUri).await()
            Log.d("CollegeDetails","Image uploaded")
            val downloadUrl = imageRef.downloadUrl.await()
            Log.d("CollegeDetails","Image URL downloaded")
             downloadUrl.toString()
        }
        catch (e : Exception){
            Log.d("CollegeDetails",e.toString())
            null
        }

    }

    suspend fun fetchCodingProfiles(userId : String) : DocumentSnapshot{
        return firebaseFirestore.collection("all_user_id").document(userId)
            .collection("all_user_details").document("coding_profiles").get().await()
    }

    suspend fun saveSkills(userId : String , listOfSkills : List<String> ){
        firebaseFirestore.collection("all_user_id").document(userId)
            .collection("all_user_details").document("user_skills").
            set(mapOf("skillList" to listOfSkills)).await()
    }
    suspend fun fetchSkills(userId : String) : DocumentSnapshot{
        return firebaseFirestore.collection("all_user_id").document(userId)
            .collection("all_user_details").document("user_skills").get().await()
    }
}