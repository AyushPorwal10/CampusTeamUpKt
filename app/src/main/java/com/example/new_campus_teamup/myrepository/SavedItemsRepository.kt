package com.example.new_campus_teamup.myrepository

import android.util.Log
import com.example.new_campus_teamup.UiState
import com.example.new_campus_teamup.clean_code_1.DeletePostConfig
import com.example.new_campus_teamup.mydataclass.ProjectDetails
import com.example.new_campus_teamup.mydataclass.RoleDetails
import com.example.new_campus_teamup.mydataclass.VacancyDetails
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SavedItemsRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) {
    suspend fun deleteSavedProject(config: DeletePostConfig) {
        try {
            firebaseFirestore.collection("all_user_id")
                .document(config.userId!!) // already check not null in handler
                .collection("project_saved")
                .document(config.postId)
                .delete()
                .await()
        }
        catch (e : Exception){
            throw e
        }
    }
    suspend fun deleteSavedRole(config: DeletePostConfig) {
        try {
            firebaseFirestore.collection("all_user_id")
                .document(config.userId!!) // already check not null in handler
                .collection("saved_roles")
                .document(config.postId)
                .delete()
                .await()
        }
        catch (e : Exception){
            throw e
        }
    }
    suspend fun deleteSavedVacancy(config: DeletePostConfig) {
        try {
            firebaseFirestore.collection("all_user_id")
                .document(config.userId!!) // already check not null in handler
                .collection("saved_vacancy")
                .document(config.postId)
                .delete()
                .await()
        }
        catch (e : Exception){
            throw e
        }
    }
    fun fetchSavedRoles(currentUserId: String): Flow<List<RoleDetails>> =
        fetchSavedData(currentUserId, "saved_roles")

    fun fetchSavedProjects(currentUserId: String): Flow<List<ProjectDetails>> =
        fetchSavedData(currentUserId, "project_saved")

    fun fetchSavedVacancies(currentUserId: String): Flow<List<VacancyDetails>> =
        fetchSavedData(currentUserId, "saved_vacancy")

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

    fun reportPost(
        tag: String,
        postId: String,
        userId: String,
        onStateChange: (UiState<Unit>) -> Unit
    ) {
        try {
            onStateChange(UiState.Loading)
            firebaseFirestore.collection("reported_posts").document(userId).collection(tag)
                .document(postId).set(mapOf("post_id" to postId)).addOnSuccessListener {
                    onStateChange(UiState.Success(Unit))
                }.addOnFailureListener {
                    onStateChange(UiState.Error(it.message ?: "Unexpected error"))
                }
        } catch (exception: Exception) {
            onStateChange(UiState.Error(exception.message ?: "Unexpected error"))
        }
    }

}