package com.example.campus_teamup.myrepository

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TeamDetailsRepository @Inject constructor(
   val  firebaseFirestore: FirebaseFirestore
) {



     fun fetchUserName(query : String , onUpdate : (List<String>) -> Unit  , onError : (Exception)-> Unit ){


        val userIdReference = firebaseFirestore.collection("search_user_id")
        val userIdList = mutableListOf<String>()

        userIdReference.get()
            .addOnSuccessListener {querySnapshot->

                for (document in querySnapshot.documents) {
                    val userId = document.getString("userId")

                    if (userId != null) {
                        if (userId.contains(query, ignoreCase = true)) {
                            userIdList.add(userId)
                        }
                    } else {
                        Log.d("UserName", "Document ${document.id} has no userId field!")
                    }
                }
                Log.d("UserName", "Final user list: $userIdList")
                onUpdate(userIdList)

                Log.d("UserName","For Loop is End")
                onUpdate(userIdList)
            }
            .addOnFailureListener{exception->
                onError(exception)
            }
    }

    fun checkIfUserInOtherTeam(collegeName:String , listOfTeamMembers: SnapshotStateList<String> , isPresent : (Boolean)-> Unit , onError: (Exception) -> Unit) {

        val membersToCheck = listOfTeamMembers.drop(1).take(6)

        if (membersToCheck.isEmpty()) {
            isPresent(false)
            return
        }

        firebaseFirestore.collection("all_teams").document(collegeName).collection("teams")
            .whereIn(FieldPath.documentId(), membersToCheck)
            .get()
            .addOnSuccessListener { querySnapshot ->
                isPresent(querySnapshot.isEmpty.not())
            }
            .addOnFailureListener {
                Log.d("TeamDetails", it.toString())
                onError(it)
            }
    }

    suspend fun saveTeamDetails(collegeName: String,listOfTeamMembers: SnapshotStateList<String> , userId : String) = coroutineScope{
        val teamMembers = hashMapOf(
            "members" to listOfTeamMembers.toList()
        )
        val firstDocumentRef = firebaseFirestore.collection("all_teams").document(collegeName).collection("teams")
            .document(listOfTeamMembers[0])

        firstDocumentRef.set(teamMembers).await()

        val teamDetailsPath = hashMapOf(
            "teamReference" to firstDocumentRef.path
        )

        firebaseFirestore.collection("all_user_id")
            .document(userId)
            .collection("all_user_details")
            .document("teamDetails")
            .set(teamDetailsPath)
            .await()
    }

    suspend fun fetchTeamDetails(userId: String) : List<String>{
        val teamDetailsSnapshot = firebaseFirestore.collection("all_user_id")
            .document(userId)
            .collection("all_user_details")
            .document("teamDetails")
            .get()
            .await()

        val teamDetailsReferencePath = teamDetailsSnapshot.getString("teamReference")

        if(teamDetailsReferencePath.isNullOrEmpty()){
            return emptyList()
        }

        val teamSnapshot = firebaseFirestore.document(teamDetailsReferencePath).get().await()
        return teamSnapshot.get("members") as? List<String> ?: emptyList()

    }
}