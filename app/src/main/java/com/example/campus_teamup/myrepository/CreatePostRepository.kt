package com.example.campus_teamup.myrepository

import android.net.Uri
import android.util.Log
import com.example.campus_teamup.mydataclass.RoleDetails
import com.example.campus_teamup.mydataclass.VacancyDetails
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import java.net.URI
import javax.inject.Inject


class CreatePostRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val storageReference: StorageReference
){


    suspend fun postRole(userId : String, roleDetails: RoleDetails){
        firebaseFirestore.collection("all_user_id").document(userId).
        collection("all_user_details").document("role_posted")
            .collection("roles").add(roleDetails).await()
        Log.d("PostRole","Role posted successfully")
    }


    suspend fun uploadTeamLogo(teamLogoUri : Uri) : String?{
        return try {

            val imageRef = storageReference.child("team_logo/${System.currentTimeMillis()}.jpg")

            imageRef.putFile(teamLogoUri).await()
            Log.d("Vacancy","Logo uploaded")
            val downloadUrl = imageRef.downloadUrl.await()
            Log.d("Vacancy","Logo URL downloaded")
            downloadUrl.toString()
        }
        catch (e : Exception){
            Log.d("Vacancy",e.toString())
            null
        }

    }
    suspend fun postVacancy(userId : String ,vacancyDetails: VacancyDetails ){
        firebaseFirestore.collection("all_user_id").document(userId).
        collection("all_user_details").document("vacancy_posted")
            .collection("vacancy").add(vacancyDetails).await()
        Log.d("Vacancy","Vacancy posted successfully")
    }

}