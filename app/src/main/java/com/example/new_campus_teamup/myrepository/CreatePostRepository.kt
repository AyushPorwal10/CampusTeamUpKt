package com.example.new_campus_teamup.myrepository

import android.util.Log
import androidx.core.net.toUri
import com.example.new_campus_teamup.mydataclass.ProjectDetails
import com.example.new_campus_teamup.mydataclass.RoleDetails
import com.example.new_campus_teamup.mydataclass.VacancyDetails
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class CreatePostRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val storageReference: StorageReference
) {
    suspend fun fetchImageUrlFromUserDetails(userId: String): DocumentSnapshot {
        return firebaseFirestore.collection("user_images").document(userId).get().await()
    }


    suspend fun uploadTeamLogo(userId: String, teamLogoUri: String , canPostVacancy : (Boolean , String?) -> Unit ){
         try {
            if(!canPost("vacancy_posted", userId, 4)){
                canPostVacancy(false , "")
                return
            }
            val imageRef = storageReference.child("team_logo/$userId/teamLogo.jpg")

            imageRef.putFile(teamLogoUri.toUri()).await()
            val downloadUrl = imageRef.downloadUrl.await()
             canPostVacancy(true , downloadUrl.toString())
        } catch (e: Exception) {
             canPostVacancy(true , null)
        }
    }

    suspend fun canPost(subCollection : String , userId: String , limit : Int) : Boolean{
        val snapshot =
            firebaseFirestore.collection("all_user_id").document(userId).collection(subCollection)
                .get()
                .await()

        val count = snapshot.size()

        return count < limit
    }
}