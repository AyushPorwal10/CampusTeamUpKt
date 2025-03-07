package com.example.campus_teamup.myrepository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ViewVacancyRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) {


    fun fetchTeamDetails(userId: String): Flow<List<String>> = callbackFlow {
        Log.d("Team", userId)
        val teamDetailsSnapshot = firebaseFirestore.collection("all_user_id").document(userId)
            .collection("all_user_details").document("teamDetails").get().await()

        val teamReferencePath =
            teamDetailsSnapshot.getString("teamReference") ?: return@callbackFlow
        Log.d("Team", teamReferencePath)
        val realTimeTeamDetailsUpdate =
            firebaseFirestore.document(teamReferencePath).addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val listOfTeamMembers = snapshot?.get("members") as? List<String> ?: emptyList()
                Log.d("Team","Size of team in repository is ${listOfTeamMembers.size}")

                trySend(listOfTeamMembers)
            }
        awaitClose { realTimeTeamDetailsUpdate.remove() }
    }

    suspend fun fetchMemberImage(userIds : List<String>) : Map<String , String>{
        val imageMap = mutableMapOf<String, String>()

        return try{
            val tasks = userIds.map { userId->
                firebaseFirestore.collection("user_images").document(userId).get()
            }
            val results = tasks.map { it.await() }

            for((index , document ) in results.withIndex()){
                val userId = userIds[index]
                val imageUrl = document.getString("user_image") ?: ""
                imageMap[userId] = imageUrl
            }


            imageMap
        }
        catch (e : Exception){
            Log.e("Testing", "Error fetching user images", e)
            userIds.associateWith { "" } // Return map with empty strings on failure
        }
    }
}