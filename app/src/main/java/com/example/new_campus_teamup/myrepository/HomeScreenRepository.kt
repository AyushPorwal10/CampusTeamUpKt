package com.example.new_campus_teamup.myrepository

import android.util.Log
import com.example.new_campus_teamup.myactivities.UserManager
import com.example.new_campus_teamup.mydataclass.ProjectDetails
import com.example.new_campus_teamup.mydataclass.RoleDetails
import com.example.new_campus_teamup.mydataclass.VacancyDetails
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HomeScreenRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val userManager: UserManager
) {

    suspend fun getUserImageUrl(currentUserId: String) : Flow<String> = callbackFlow {

        val documentReference = firebaseFirestore.collection("user_images").document(currentUserId)

        val realTimeImageFetching = documentReference.addSnapshotListener{snapshot , error->
            if(error != null){
                close(error)
                return@addSnapshotListener
            }

            if(snapshot != null && snapshot.exists()) {
                val imageUrl = snapshot.getString("user_image") as String
                Log.d("ImageLoading","Current user image loaded : $imageUrl")
                trySend(imageUrl)
            }
        }
        awaitClose{realTimeImageFetching.remove()}
    }

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

            Log.d("Roles", "Repository On Refresh getting new roles ${roles.size}")

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

    suspend fun fetchProjects(lastProject: DocumentSnapshot?): QuerySnapshot {
        var query = firebaseFirestore.collection("all_projects")
            .orderBy("postedOn", Query.Direction.DESCENDING)
            .limit(15)

        lastProject?.let {
            query = query.startAfter(lastProject)
        }
        return query.get().await()
    }


    suspend fun saveFcmToken(fcmToken: String, userId: String) {
        // this can be when user log out and login again , it will cause fcm to empty since fcm always generate when
        // user open app for first time
        if (fcmToken.isEmpty())
            return
        Log.d("FCM", "FCM is saved in firestore $fcmToken")
        firebaseFirestore.collection("all_fcm").document(userId)
            .set(mapOf("fcm_token" to fcmToken), SetOptions.merge()).await()
    }

    suspend fun saveProject(
        phoneNumber: String,
        projectDetails: ProjectDetails,
        onProjectSaved: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        savedItem(
            phoneNumber,
            "project_saved",
            projectDetails.projectId,
            projectDetails,
            "SavingProject",
            {
                onProjectSaved()
            },
            {
                onError(it)
            })
    }

    suspend fun saveRole(
        phoneNumber: String,
        roleDetails: RoleDetails,
        onRoleSaved: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        savedItem(phoneNumber, "saved_roles", roleDetails.roleId, roleDetails, "SaveRole", {
            onRoleSaved()
        }, {
            onError(it)
        })
    }
    suspend fun saveVacancy(
        phoneNumber: String,
        vacancyDetails: VacancyDetails,
        onVacancySaved: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        savedItem(phoneNumber, "saved_vacancy", vacancyDetails.vacancyId, vacancyDetails, "SavingVacancies", {
            onVacancySaved()
        }, {
            onError(it)
        })
    }


    private suspend inline fun <T : Any> savedItem(
        phoneNumber: String,
        collection: String,
        itemId: String,
        item: T,
        logTag: String,
        onItemSaved: () -> Unit,
        onError: (Exception) -> Unit
    ) {

        try {
            firebaseFirestore.collection("all_user_id").document(phoneNumber)
                .collection(collection).document(itemId)
                .set(item).await()
            Log.d(logTag, "Items saved success")
            onItemSaved()
        } catch (e: Exception) {
            Log.d(logTag, "Repo error $e")
            onError(e)
        }
    }


    fun fetchCurrentUserSavedPost(phoneNumber: String): Flow<List<String>> =
        fetchSavedItems(phoneNumber, "project_saved", "Saving")

    fun fetchCurrentUserSavedRole(phoneNumber: String): Flow<List<String>> =
        fetchSavedItems(phoneNumber, "saved_roles", "FetchedRole")

    fun fetchCurrentUserSavedVacancy(phoneNumber: String): Flow<List<String>> =
        fetchSavedItems(phoneNumber, "saved_vacancy", "FetchedVacancy")


    // this can be projects , roles , vacancies
    private fun fetchSavedItems(
        phoneNumber: String,
        subCollection: String,
        logTag: String
    ): Flow<List<String>> = callbackFlow {

        val collectionReference =
            firebaseFirestore.collection("all_user_id").document(phoneNumber)
                .collection(subCollection)
        val listener = collectionReference.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.d(logTag, "Error fetching data: $error")
                close(error)
                return@addSnapshotListener
            }
            val savedIds = snapshot?.documents?.map { it.id } ?: emptyList()
            Log.d(logTag, "Fetched size: ${savedIds.size} <-")
            trySend(savedIds).isSuccess // Ensure safe sending
        }
        awaitClose { listener.remove() }
    }
}