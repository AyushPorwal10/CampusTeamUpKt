package com.example.campus_teamup.myrepository

import android.net.Uri
import android.util.Log
import com.example.campus_teamup.mydataclass.ProjectDetails
import com.example.campus_teamup.mydataclass.RoleDetails
import com.example.campus_teamup.mydataclass.VacancyDetails
import com.google.firebase.firestore.DocumentSnapshot
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
        val userRoleRef = firebaseFirestore.collection("all_user_id").document(userId).
        collection("all_user_details").document("role_posted")
            .collection("roles").add(roleDetails).await()


        firebaseFirestore
            .collection("all_roles")
            .document(userRoleRef.id)
            .set(roleDetails)
            .await()


        Log.d("PostRole","Role posted successfully")
    }
    suspend fun fetchImageUrlFromUserDetails(userId : String) : DocumentSnapshot{
        return   firebaseFirestore.collection("all_user_id").document(userId).
        collection("all_user_details").document("college_details").get().await()
    }


    suspend fun uploadTeamLogo(userId: String , teamLogoUri : Uri) : String?{
        return try {

            val imageRef = storageReference.child("team_logo/$userId/teamLogo.jpg")

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
        val teamVacancyReference = firebaseFirestore.collection("all_user_id").document(userId).
        collection("all_user_details").document("vacancy_posted")
            .collection("vacancy").add(vacancyDetails).await()


        firebaseFirestore.collection("all_vacancy")
            .document(teamVacancyReference.id)
            .set(vacancyDetails)
            .await()

        Log.d("Vacancy","Vacancy posted successfully")
    }

    suspend fun postProject(userId: String , projectDetails: ProjectDetails){

       val projectReference =  firebaseFirestore.collection("all_user_id").document(userId)
            .collection("all_user_details").document("project_posted")
            .collection("project").add(projectDetails).await()

        firebaseFirestore.collection("all_projects").document(projectReference.id).set(projectDetails).await()


    }

}