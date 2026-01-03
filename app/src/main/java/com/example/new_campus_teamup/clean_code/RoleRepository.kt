package com.example.new_campus_teamup.clean_code

import android.util.Log
import com.example.new_campus_teamup.mydataclass.RoleDetails
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RoleRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
) {

    suspend fun getPostedRoleCount(userId: String): Int {
        return firebaseFirestore
            .collection("all_user_id")
            .document(userId)
            .collection("roles_posted")
            .get()
            .await()
            .size()
    }

    suspend fun postRole(
        details: RoleDetails
    ) {
        try {
            val postId = generateRoleId()
           val roleDetails = details.copy(postId = postId)

            firebaseFirestore
                .collection("all_user_id")
                .document(roleDetails.postedBy)
                .collection("roles_posted")
                .document(postId)
                .set(roleDetails)
                .await()

            firebaseFirestore
                .collection("all_roles")
                .document(postId)
                .set(roleDetails)
                .await()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun fetchImageUrlFromUserDetails(userId: String): String {
        return firebaseFirestore.collection("user_images").document(userId).get().await().get("user_image")?.toString() ?: ""
    }


     fun deleteRole(config: DeletePostConfig ){
        try{
            val batch = firebaseFirestore.batch()

            val allPostReference  = firebaseFirestore.collection("all_roles").document(config.postId)
            val userPostReference = firebaseFirestore.collection("all_user_id").document(config.userId).collection("roles_posted").document(config.postId)

            batch.delete(allPostReference)
            batch.delete(userPostReference)

            batch.commit()

        }
        catch (e : Exception){
            throw e
        }
    }

    private fun generateRoleId(): String {
        return firebaseFirestore.collection("all_roles").document().id
    }

}