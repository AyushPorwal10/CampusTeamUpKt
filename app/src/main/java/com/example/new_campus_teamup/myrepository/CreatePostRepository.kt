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


    suspend fun postRole(
        collegeName : String ,
        userId: String,
        userName: String,
        userImage: String,
        role: String,
        datePosted: String,
        canPostRole: (Boolean) -> Unit,
    ) {

        // checking if total role posted is less than 3

        if(!canPost("roles_posted" , userId , 3))
            return canPostRole(false)

        val generatedRoleId = firebaseFirestore.collection("all_roles").document().id
        Log.d("SavedRoles", "Normal id i am getting is $generatedRoleId")

        val roleDetails = RoleDetails(
            "123456",
            collegeName,
            generatedRoleId,
            userId,
            userName,
            userImage,
            role,
            datePosted
        )



        firebaseFirestore.collection("all_user_id").document(userId).collection("roles_posted")
            .document(generatedRoleId).set(roleDetails).await()

        firebaseFirestore.collection("all_roles").document(generatedRoleId).set(roleDetails).await()


        canPostRole(true)

        Log.d("PostRole", "Role posted successfully")
    }

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
            Log.d("Vacancy", "Image uri is ${teamLogoUri.toUri()}")

            imageRef.putFile(teamLogoUri.toUri()).await()
            Log.d("Vacancy", "Logo uploaded")
            val downloadUrl = imageRef.downloadUrl.await()
            Log.d("Vacancy", "Logo URL downloaded")
             canPostVacancy(true , downloadUrl.toString())
        } catch (e: Exception) {
            Log.d("Vacancy", e.toString())
             canPostVacancy(true , null)
        }
    }


    fun postTeamVacancy(userId: String, vacancyDetails: VacancyDetails) {
        firebaseFirestore.runTransaction { transaction ->

            val vacancyId = firebaseFirestore.collection("all_vacancy").document().id

            Log.d("SavingVacancy", "Vacancy id is $vacancyId <-")
            vacancyDetails.vacancyId = vacancyId

            Log.d("SavingVacancy", "Vacancy details updated with id ${vacancyDetails.vacancyId} <-")

            val toUserDetails = firebaseFirestore.collection("all_user_id").document(userId)
                .collection("vacancy_posted")
                .document(vacancyId)

            val toAllVacancy = firebaseFirestore.collection("all_vacancy").document(vacancyId)

            transaction.set(toUserDetails, vacancyDetails)
            transaction.set(toAllVacancy, vacancyDetails)
        }
    }


    fun addProject(
        userId: String,
        postedOn: String,
        teamName: String,
        hackathonOrPersonal: String,
        problemStatement: String,
        githubUrl: String,
        projectLikes: Int
    ) {


        val batch = firebaseFirestore.batch()


        // getting id where project will be stored
        val projectReferenceId = firebaseFirestore.collection("all_projects").document()
        Log.d("Project", "Project document id is ${projectReferenceId.id}")

        val projectDetails = ProjectDetails(
            projectReferenceId.id,
            userId,
            postedOn,
            teamName,
            hackathonOrPersonal,
            problemStatement,
            githubUrl,
            projectLikes
        )

        // adding project
        val addProject =
            firebaseFirestore.collection("all_projects").document(projectReferenceId.id)

        val updateProjectPostedList = firebaseFirestore.collection("all_user_id").document(userId).collection("project_posted").document(projectReferenceId.id)

        batch.set(updateProjectPostedList , projectDetails)

        batch.set(addProject, projectDetails)

        batch.commit()

    }

    suspend fun canPost(subCollection : String , userId: String , limit : Int) : Boolean{
        val snapshot =
            firebaseFirestore.collection("all_user_id").document(userId).collection(subCollection)
                .get()
                .await()  // suspend function — this is KEY to linear flow

        val count = snapshot.size()

        return count < limit

    }
}