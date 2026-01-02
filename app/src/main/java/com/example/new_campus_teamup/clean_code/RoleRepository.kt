package com.example.new_campus_teamup.clean_code

import com.example.new_campus_teamup.mydataclass.RoleDetails
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RoleRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val storageReference: StorageReference
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
        roleDetails: RoleDetails
    ) {
        firebaseFirestore
            .collection("all_user_id")
            .document(roleDetails.postedBy)
            .collection("roles_posted")
            .document(generateRoleId())
            .set(roleDetails)
            .await()

        firebaseFirestore
            .collection("all_roles")
            .document(generateRoleId())
            .set(roleDetails)
            .await()
    }


    private fun generateRoleId(): String {
        return firebaseFirestore.collection("all_roles").document().id
    }

}