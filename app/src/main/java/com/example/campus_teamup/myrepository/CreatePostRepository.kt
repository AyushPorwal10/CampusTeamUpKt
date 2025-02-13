package com.example.campus_teamup.myrepository

import android.util.Log
import com.example.campus_teamup.mydataclass.RoleDetails
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class CreatePostRepository @Inject constructor(
    val firebaseFirestore: FirebaseFirestore
){


    suspend fun postRole(userId : String, roleDetails: RoleDetails){
        firebaseFirestore.collection("all_user_id").document(userId).
        collection("all_user_details").document("role_posted")
            .collection("roles").add(roleDetails).await()
        Log.d("PostRole","Role posted successfully")
    }

}