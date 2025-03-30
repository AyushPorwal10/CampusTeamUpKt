package com.example.campus_teamup.myrepository

import android.util.Log
import com.example.campus_teamup.mydataclass.CollegeDetails
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ViewProfileRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) {
    fun fetchCollegeDetails(userId: String): Flow<CollegeDetails> = callbackFlow {

        val documentReference = firebaseFirestore.collection("all_user_id")
            .document(userId).collection("all_user_details").document("college_details")

        val realTimeListener = documentReference.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            snapshot?.toObject(CollegeDetails::class.java)?.let { collegeDetails ->
                trySend(collegeDetails).isSuccess
            }
        }
        awaitClose { realTimeListener.remove() }
    }

    fun fetchCodingProfilesDetails(userId: String): Flow<List<String>> = callbackFlow {

        val documentReference = firebaseFirestore.collection("all_user_id")
            .document(userId).collection("all_user_details").document("coding_profiles")

        val realTimeListener = documentReference.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.d("Coding" , "Error in repo $error")
                close(error)
                return@addSnapshotListener
            }

            val profileList = snapshot?.get("profilelist") as? List<String> ?: emptyList()
            trySend(profileList).isSuccess
        }
        awaitClose { realTimeListener.remove() }
    }

    fun fetchSkills(userId: String) : Flow<List<String>> = callbackFlow{
        val documentReference  = firebaseFirestore.collection("all_user_id")
            .document(userId).collection("all_user_details").document("user_skills")

        val realTimeListener = documentReference.addSnapshotListener{snapshot , error->
            if(error != null){
                close(error)
                return@addSnapshotListener
            }

            val skillsList = snapshot?.get("skillList") as? List<String> ?: emptyList()
            trySend(skillsList)
        }
        awaitClose{realTimeListener.remove()}
    }

}