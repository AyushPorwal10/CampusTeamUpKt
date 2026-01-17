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
    fun observeUserPostedRoles(userId: String): Flow<List<RoleDetails>> = callbackFlow {
        val listener = firestore.collection("all_user_id")
            .document(userId)
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

    fun observeUserPostedVacancy(userId: String): Flow<List<VacancyDetails>> = callbackFlow {
        val listener = firestore
            .collection("all_user_id")
            .document(userId)
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
    fun observeUserPostedProjects(userId: String): Flow<List<ProjectDetails>> = callbackFlow {
        val listener = firestore
            .collection("all_user_id")
            .document(userId)
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
}