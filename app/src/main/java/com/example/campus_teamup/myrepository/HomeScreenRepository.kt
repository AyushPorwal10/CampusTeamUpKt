package com.example.campus_teamup.myrepository

import android.util.Log
import com.example.campus_teamup.mydataclass.ProjectDetails
import com.example.campus_teamup.mydataclass.RoleDetails
import com.example.campus_teamup.mydataclass.VacancyDetails
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HomeScreenRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) {

    suspend fun fetchInitialOrPaginatedRoles(lastVisible: DocumentSnapshot?): QuerySnapshot {

        return if (lastVisible == null) {
            Log.d("Roles", "HomeScreenRepository Going to Paginated roles first time")
            firebaseFirestore.collection("all_roles")
                .orderBy("postedOn", Query.Direction.DESCENDING)
                .limit(15)
                .get()
                .await()
        } else {
            Log.d("Roles", "HomeScreenRepository Going to Paginated second time")
            firebaseFirestore.collection("all_roles")
                .orderBy("postedOn", Query.Direction.DESCENDING)
                .startAfter(lastVisible)
                .limit(15)
                .get()
                .await()
        }
    }


    fun observeRoles(
        lastVisible: DocumentSnapshot?,
        onUpdate: (List<RoleDetails>, DocumentSnapshot?) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val query = if (lastVisible == null) {
            firebaseFirestore.collection("all_roles")
                .orderBy("postedOn", Query.Direction.DESCENDING)
                .limit(15)
        } else {
            firebaseFirestore.collection("all_roles")
                .orderBy("postedOn", Query.Direction.DESCENDING)
                .startAfter(lastVisible)
                .limit(15)
        }

        query.addSnapshotListener { querySnapshot, error ->
            if (error != null) {
                onError(error)
                return@addSnapshotListener
            }

            val roles = querySnapshot?.documents?.mapNotNull { doc ->
                doc.toObject(RoleDetails::class.java)
            } ?: emptyList()

            val newLastVisible = querySnapshot?.documents?.lastOrNull()
            onUpdate(roles, newLastVisible)
        }
    }

    // Vacancy Section for observing and fetching initial and paginated vacancy


    suspend fun fetchInitialOrPaginatedVacancy(lastVisible: DocumentSnapshot?): QuerySnapshot {

        return if (lastVisible == null) {
            firebaseFirestore.collection("all_vacancy")
                .orderBy("postedOn", Query.Direction.DESCENDING)
                .limit(15)
                .get()
                .await()
        } else {
            firebaseFirestore.collection("all_vacancy")
                .orderBy("postedOn", Query.Direction.DESCENDING)
                .limit(15)
                .startAfter(lastVisible)
                .get()
                .await()
        }
    }

    fun observeVacancy(
        lastVisible: DocumentSnapshot?,
        onUpdate: (List<VacancyDetails>, DocumentSnapshot?) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val query = if (lastVisible == null) {
            firebaseFirestore.collection("all_vacancy")
                .orderBy("postedOn", Query.Direction.DESCENDING)
                .limit(15)


        } else {
            firebaseFirestore.collection("all_vacancy")
                .orderBy("postedOn", Query.Direction.DESCENDING)
                .startAfter(lastVisible)
        }

        query.addSnapshotListener { querySnapshot, error ->

            if (error != null) {
                onError(error)
                return@addSnapshotListener
            }

            val vacancy = querySnapshot?.documents?.mapNotNull { doc ->
                doc.toObject(VacancyDetails::class.java)
            } ?: emptyList()

            val newLastVisible = querySnapshot?.documents?.lastOrNull()
            onUpdate(vacancy, newLastVisible)
        }
    }

    // Project Section
    fun observeProjects(
        onError: (Exception) -> Unit,
        onUpdate: (List<ProjectDetails>) -> Unit
    ) {

        val query = firebaseFirestore.collection("all_projects")
            .orderBy("projectLikes", Query.Direction.DESCENDING)
            .limit(15)

        query.addSnapshotListener { querySnapshot, error ->

            if (error != null) {
                onError(error)
                return@addSnapshotListener
            }

            val projects = querySnapshot?.documents?.mapNotNull { doc ->
                doc.toObject(ProjectDetails::class.java)
            } ?: emptyList()

            onUpdate(projects)
        }
    }


    suspend fun fetchPaginatedProjects() : QuerySnapshot{
        return firebaseFirestore.collection("all_projects").orderBy("projectLikes",Query.Direction.DESCENDING)
            .limit(15)
            .get()
            .await()
    }
}