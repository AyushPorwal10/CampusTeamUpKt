package com.example.new_campus_teamup.yourposts

import android.util.Log
import com.example.new_campus_teamup.mydataclass.ProjectDetails
import com.example.new_campus_teamup.mydataclass.RoleDetails
import com.example.new_campus_teamup.mydataclass.VacancyDetails
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class YourPostRepo @Inject constructor(private val firestore: FirebaseFirestore) {


    val tag = "YourPost"



    // this is posted by user that user can delete
    fun observeUserPostedRoles(phoneNumber: String): Flow<List<RoleDetails>> = callbackFlow {
        val listener = firestore.collection("all_user_id")
            .document(phoneNumber)
            .collection("roles_posted")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val roles = snapshot?.documents?.mapNotNull { it.toObject(RoleDetails::class.java) } ?: emptyList()
                trySend(roles)
            }

        awaitClose { listener.remove() }
    }

    fun observeUserPostedVacancy(phoneNumber: String): Flow<List<VacancyDetails>> = callbackFlow {
        val listener = firestore
            .collection("all_user_id")
            .document(phoneNumber)
            .collection("vacancy_posted")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val vacancyList = snapshot?.documents?.mapNotNull { it.toObject(VacancyDetails::class.java) } ?: emptyList()
                trySend(vacancyList).isSuccess
            }

        awaitClose { listener.remove() }
    }



    fun observeUserPostedProjects(phoneNumber: String): Flow<List<ProjectDetails>> = callbackFlow {
        val listener = firestore
            .collection("all_user_id")
            .document(phoneNumber)
            .collection("project_posted")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val projectList = snapshot?.documents?.mapNotNull { it.toObject(ProjectDetails::class.java) } ?: emptyList()
                trySend(projectList).isSuccess
            }

        awaitClose { listener.remove() }
    }




    fun deleteRole(roleId : String , phoneNumber : String , onRoleDelete : () -> Unit  , onError : () -> Unit ){
        deletePost(roleId , phoneNumber , "all_roles","roles_posted", onPostDelete = {
            onRoleDelete()
        } , onError = {
            onError()
        })
    }
    fun deleteVacancy(vacancyId : String , phoneNumber : String , onVacancyDelete : () -> Unit  , onError : () -> Unit ){
        deletePost(vacancyId , phoneNumber , "all_vacancy","vacancy_posted", onPostDelete = {
            onVacancyDelete()
        } , onError = {
            onError()
        })
    }
    fun deleteProject(projectId : String , phoneNumber : String , onProjectDelete : () -> Unit  , onError : () -> Unit ){
        deletePost(projectId , phoneNumber , "all_projects","project_posted", onPostDelete = {
            onProjectDelete()
        } , onError = {
            onError()
        })
    }

    private fun deletePost(postId : String , phoneNumber : String , commonPostCollection : String , userPostCollection : String , onPostDelete : () -> Unit , onError : () -> Unit ){

        try{
            val batch = firestore.batch()

            val allPostReference  = firestore.collection(commonPostCollection).document(postId)
            val userPostReference = firestore.collection("all_user_id").document(phoneNumber).collection(userPostCollection).document(postId)

            batch.delete(allPostReference)
            batch.delete(userPostReference)

            batch.commit()

            onPostDelete()
        }
        catch (e : Exception){
            Log.d(tag , "Error deleting $commonPostCollection  , $userPostCollection")
            onError()
        }

    }


}