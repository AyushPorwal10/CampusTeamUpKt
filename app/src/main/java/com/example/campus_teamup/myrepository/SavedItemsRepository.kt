package com.example.campus_teamup.myrepository

import android.util.Log
import com.example.campus_teamup.mydataclass.ProjectDetails
import com.example.campus_teamup.mydataclass.RoleDetails
import com.example.campus_teamup.mydataclass.VacancyDetails
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SavedItemsRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) {

    suspend fun unSaveProject(currentUserId: String, projectId: String) {
        firebaseFirestore.collection("all_user_id").document(currentUserId)
            .collection("project_saved").document(projectId).delete()
            .addOnFailureListener {
                Log.d("FetchingProjects", "Error Unsaving project")
            }.await()
    }

    suspend fun unSaveRole(currentUserId: String, roleId: String) {
        firebaseFirestore.collection("all_user_id").document(currentUserId)
            .collection("saved_roles").document(roleId).delete()
            .addOnFailureListener {
                Log.d("UnsaveRole", "Error Unsaving roles")
            }.await()
    }

    suspend fun unSaveVacancy(currentUserId: String, vacancyId: String) {
        firebaseFirestore.collection("all_user_id").document(currentUserId)
            .collection("saved_vacancy").document(vacancyId).delete()
            .addOnFailureListener {
                Log.d("UnsaveVacancy", "Error Unsaving vacancy")
            }.await()
    }


    fun fetchSavedRoles(currentUserId: String): Flow<List<RoleDetails>> =
        fetchSavedData(currentUserId, "saved_roles")

    fun fetchSavedProjects(currentUserId: String): Flow<List<ProjectDetails>> =
        fetchSavedData(currentUserId, "project_saved")

    fun fetchSavedVacancies(currentUserId: String) : Flow<List<VacancyDetails>> =
        fetchSavedData(currentUserId , "saved_vacancy")

    private inline fun <reified T> fetchSavedData(
        currentUserId: String,
        collection: String
    ): Flow<List<T>> = callbackFlow {

        val collectionReference =
            firebaseFirestore.collection("all_user_id").document(currentUserId)
                .collection(collection)

        val listener = collectionReference.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val list = snapshot?.documents?.mapNotNull { it.toObject(T::class.java) } ?: emptyList()
            trySend(list)
            Log.d("FetchingData", "Fetched $collection list size: ${list.size} <-")
        }
        awaitClose { listener.remove() }
    }


}