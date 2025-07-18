package com.example.new_campus_teamup.myrepository

import android.net.Uri
import android.util.Log
import com.example.new_campus_teamup.myactivities.UserManager
import com.example.new_campus_teamup.mydataclass.EducationDetails
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserProfileRepo @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val storageReference: StorageReference,
    private val userManager: UserManager
) {
    // college Details

    suspend fun saveEducationDetails(userId: String, educationDetails: EducationDetails) {
        Log.d("CollegeDetails", "Saving CollegeDetails data")
        Log.d("Parallel","Going to start D1")
         val deferred1 = firebaseFirestore.collection("all_user_id").document(userId).collection("all_user_details")
            .document("college_details").set(educationDetails)
        Log.d("Parallel","Going to start D2")


    }

    suspend fun saveUserImage(userId : String, userImageUrl : Uri , onSuccess : () -> Unit ){

        val userImageDownloadUrl = uploadUserImageToStorage(userId , userImageUrl)

        val deferred = firebaseFirestore.collection("user_images").document(userId)
            .set(mapOf("user_image" to userImageDownloadUrl))

        deferred.await()

        onSuccess()
    }

     fun observeCurrentUserImage(currentUserId : String) : Flow<String?> = callbackFlow{
        Log.d("CollegeDetails","Current User id is $currentUserId")
        val documentReference = firebaseFirestore.collection("user_images").document(currentUserId)

        val realTimeImageFetching = documentReference.addSnapshotListener{snapshot , error->
            if(error != null){
                close(error)
                return@addSnapshotListener
            }

            if(snapshot != null && snapshot.exists()) {
                val imageUrl = snapshot.getString("user_image") as? String
                Log.d("CollegeDetails","Current user image loaded : $imageUrl")
                trySend(imageUrl)
            }
        }
        awaitClose{realTimeImageFetching.remove()}
    }

    suspend fun fetchEducationDetails(userId: String): DocumentSnapshot {
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

     fun fetchCodingProfiles(userId : String) : Flow<List<String>> = callbackFlow {

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