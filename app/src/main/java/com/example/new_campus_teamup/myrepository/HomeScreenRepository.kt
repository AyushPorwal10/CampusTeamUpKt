package com.example.new_campus_teamup.myrepository

import android.util.Log
import com.example.new_campus_teamup.UiState
import com.example.new_campus_teamup.clean_code.PostType
import com.example.new_campus_teamup.clean_code_1.RepostPostConfig
import com.example.new_campus_teamup.clean_code_1.SavePostConfig
import com.example.new_campus_teamup.mydataclass.ProjectDetails
import com.example.new_campus_teamup.mydataclass.RoleDetails
import com.example.new_campus_teamup.mydataclass.VacancyDetails
import com.example.new_campus_teamup.network.NetworkMonitor
import com.example.new_campus_teamup.roles.RolesDao
import com.example.new_campus_teamup.room.PostDao
import com.example.new_campus_teamup.room.ProjectEntity
import com.example.new_campus_teamup.room.RoleEntity
import com.example.new_campus_teamup.room.VacancyEntity
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HomeScreenRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val postDao: PostDao,
    private val networkMonitor: NetworkMonitor,
    private val roleDao : RolesDao
) {



    fun getRoles() : Flow<List<RoleDetails>> {
       return roleDao.getRoles()
    }

    suspend fun syncRoles(lastVisible: DocumentSnapshot? = null): DocumentSnapshot? {
        if (!networkMonitor.isOnline()) return null

        return try {
            var query = firebaseFirestore
                .collection("all_roles")
                .orderBy("postedOn", Query.Direction.DESCENDING)
                .limit(25)

            lastVisible?.let { query = query.startAfter(it) }

            val snapshot = query.get().await()
            val roles = snapshot.documents.mapNotNull { it.toObject(RoleDetails::class.java) }

            val isFirstPage = lastVisible == null
            if (isFirstPage) {
                roleDao.syncRoles(roles)   // refreshing new roles
            } else {
                roleDao.insertRoles(roles) // just appending roles
            }

            snapshot.documents.lastOrNull()

        } catch (e: Exception) {
            null
        }
    }

    // Vacancy Section for observing and fetching initial and paginated vacancy

    fun observeVacancy(
    ) = callbackFlow {
        trySend(UiState.Loading)

        val listenerRegistration = try {
            val reference = firebaseFirestore
                .collection("all_vacancy")
                .orderBy("postedOn", Query.Direction.DESCENDING)

            reference.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(UiState.Error(error.message ?: "Unknown Firestore error"))
                    return@addSnapshotListener
                }

                val vacancies = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(VacancyDetails::class.java)
                } ?: emptyList()

                trySend(UiState.Success(vacancies))
            }
        } catch (e: Exception) {
            trySend(UiState.Error(e.message ?: "Unexpected error"))
            close(e)
            null
        }

        awaitClose {
            listenerRegistration?.remove()
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

    suspend fun saveProject(savePostConfig: SavePostConfig) {
        val projectDetails = savePostConfig.postDto as ProjectDetails
        savePost(
            savePostConfig.savedBy,
            "project_saved",
            projectDetails.postId,
            projectDetails,
            "SavingProject"
        )
    }

    suspend fun saveRole(savePostConfig: SavePostConfig) {
        val roleDetails = savePostConfig.postDto as RoleDetails
        savePost(
            savePostConfig.savedBy,
            "saved_roles",
            roleDetails.postId,
            roleDetails,
            "SaveRole"
        )
    }

    suspend fun saveVacancy(savePostConfig: SavePostConfig) {
        val vacancyDetails = savePostConfig.postDto as VacancyDetails
        savePost(
            savePostConfig.savedBy,
            "saved_vacancy",
            vacancyDetails.postId,
            vacancyDetails,
            "SavingVacancies"
        )
    }

    suspend fun reportPost(
        repostPostConfig: RepostPostConfig,
    ) {
        val tag = getFirebaseTagFromPostType(repostPostConfig.postType)
        try {
            firebaseFirestore.collection("reported_posts")
                .document(repostPostConfig.reportedBy)
                .collection(tag)
                .document(repostPostConfig.postId).set(mapOf("post_id" to repostPostConfig.postId)).await()
        } catch (exception: Exception) {
           throw exception
        }
    }

    private fun getFirebaseTagFromPostType(postType: PostType)  : String {
        return when(postType){
            PostType.ROLE -> "roles"
            PostType.VACANCY -> "vacancies"
            PostType.PROJECT -> "projects"
        }
    }

    private suspend inline fun <T : Any> savePost(
        userId: String,
        collection: String,
        itemId: String,
        item: T,
        logTag: String
    ) {
        try {
            firebaseFirestore.collection("all_user_id").document(userId)
                .collection(collection).document(itemId)
                .set(item).await()
            Log.d(logTag, "Items saved success")
        } catch (e: Exception) {
            Log.d(logTag, "Repo error $e")
            throw e
        }
    }



    // this can be projects , roles , vacancies
     fun fetchSavedItems(
        userId: String,
        subCollection: String,
        logTag: String
    ) {

         try {
             val collectionReference = firebaseFirestore.collection("all_user_id").document(userId).collection(subCollection)
             collectionReference.addSnapshotListener { snapshot, error ->
                 if (error != null) {
                     Log.d(logTag, "Error fetching data: $error")
                     return@addSnapshotListener
                 }
                 val savedIds = snapshot?.documents?.map { it.id } ?: emptyList()

                 CoroutineScope(Dispatchers.IO).launch{
                     savePostIdsToRoom(subCollection, savedIds)
                 }
                 Log.d(logTag, "Fetched size: ${savedIds.size} <-")
             }
         }
         catch (exception : Exception){
             Log.d("FetchingSavedIds","Error fetching saved ids $exception")
         }

    }


    private suspend fun savePostIdsToRoom(subCollection: String , postIds: List<String>){
        try {
            when(subCollection){
                "project_saved" -> {
                    val ids = postIds.map { ProjectEntity(it) }
                    postDao.syncProjectIds(ids)
                    Log.d("SavedPostIdToRoom","Saving projectIds to room ${ids.size}")
                }
                "saved_roles" -> {
                    val ids = postIds.map { RoleEntity(it) }
                    postDao.syncRoleIds(ids)
                }
                "saved_vacancy" ->{
                    val ids = postIds.map { VacancyEntity(it) }
                    postDao.syncVacancyIds(ids)
                }
            }
        }
        catch (exception : Exception){
            Log.d("SavedPostIdToRoom","Error syncing for subcollection $subCollection with exception $exception")
        }

    }
    fun fetchSavedRoleIds() : Flow<List<RoleEntity>> = postDao.fetchRoleIds()

    fun fetchSavedVacancyIds() : Flow<List<VacancyEntity>> = postDao.fetchVacancyIds()

    fun fetchSavedProjectIds() : Flow<List<ProjectEntity>> = postDao.fetchProjectIds()

    fun observeCurrentUserImage(currentUserId: String): Flow<String?> = callbackFlow {
        Log.d("CollegeDetails", "Current User id is $currentUserId")
        val documentReference = firebaseFirestore.collection("user_images").document(currentUserId)

        val realTimeImageFetching = documentReference.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val imageUrl = snapshot.getString("user_image") as? String
                Log.d("CollegeDetails", "Current user image loaded : $imageUrl")
                trySend(imageUrl)
            } else {
                Log.d("CollegeDetails", "Snapshot is null")
            }
        }
        awaitClose { realTimeImageFetching.remove() }
    }


}