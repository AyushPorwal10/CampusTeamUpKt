package com.example.campus_teamup.myrepository

import android.net.Uri
import android.util.Log
import com.example.campus_teamup.myactivities.UserManager
import com.example.campus_teamup.mydataclass.CollegeDetails
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserProfileRepo @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val storageReference: StorageReference,
    private val userManager: UserManager
) {
    // college Details

    suspend fun saveCollegeDetails(userId: String, collegeDetails: CollegeDetails) {
        Log.d("CollegeDetails", "Saving CollegeDetails data")
        Log.d("Parallel","Going to start D1")
         val deferred1 = firebaseFirestore.collection("all_user_id").document(userId).collection("all_user_details")
            .document("college_details").set(collegeDetails)
        Log.d("Parallel","Going to start D2")
        // saving image at another place also to fetch easily in other section

        val deferred2 = firebaseFirestore.collection("user_images").document(userId)
            .set(mapOf("user_image" to collegeDetails.userImageUrl))

        deferred1.await()
        Log.d("Parallel","D1 Done")
        deferred2.await()
        Log.d("Parallel","D2 Done")
    }

    suspend fun fetchCollegeDetails(userId: String): DocumentSnapshot {
        return firebaseFirestore.collection("all_user_id").document(userId)
            .collection("all_user_details").document("college_details").get().await()


    }

    // coding profiles

    suspend fun saveCodingProfiles(userId: String, listOfCodingProfiles: List<String>) {
        firebaseFirestore.collection("all_user_id").document(userId)
            .collection("all_user_details").document("coding_profiles")
            .set(mapOf("profilelist" to listOfCodingProfiles)).await()
    }

    suspend fun uploadUserImageToStorage(userId: String, imageUri: Uri): String? {
        return try {

            val imageRef = storageReference.child("user_images/$userId/profile.jpg")

            imageRef.putFile(imageUri).await()
            Log.d("CollegeDetails", "Image uploaded")
            val downloadUrl = imageRef.downloadUrl.await()
            Log.d("CollegeDetails", "Image URL downloaded")
            downloadUrl.toString()
        } catch (e: Exception) {
            Log.d("CollegeDetails", e.toString())
            null
        }

    }

    suspend fun fetchCodingProfiles(userId : String) : Flow<List<String>> = callbackFlow {

        val documentRef =  firebaseFirestore.collection("all_user_id").document(userId)
            .collection("all_user_details").document("coding_profiles")


      val codingProfilesListener =   documentRef.addSnapshotListener{snapshot , error->


            if(error != null){
                close(error)
                Log.d("Coding","Error $error")
                return@addSnapshotListener
            }
            if(snapshot != null && snapshot.exists()){
                val profiles = snapshot.get("profilelist") as List<String>
                trySend(profiles)
            }
        }
        awaitClose{codingProfilesListener.remove()}
    }

    suspend fun saveSkills(userId: String, listOfSkills: List<String>) {
        firebaseFirestore.collection("all_user_id").document(userId)
            .collection("all_user_details").document("user_skills")
            .set(mapOf("skillList" to listOfSkills)).await()
    }

    suspend fun fetchSkills(userId: String): DocumentSnapshot {
        return firebaseFirestore.collection("all_user_id").document(userId)
            .collection("all_user_details").document("user_skills").get().await()
    }
}