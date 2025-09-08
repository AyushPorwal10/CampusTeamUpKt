package com.example.new_campus_teamup.myrepository

import android.util.Log
import com.example.new_campus_teamup.UiState
import com.example.new_campus_teamup.mydataclass.RoleDetails
import com.example.new_campus_teamup.mydataclass.VacancyDetails
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class SearchRoleVacancyRepository @Inject constructor(
    val firebaseFirestore: FirebaseFirestore,
) {

     fun fetchRolesFromFirebase(query: String , onStateChange  : (UiState<List<RoleDetails>>) -> Unit){

         onStateChange(UiState.Loading)
         try {
             firebaseFirestore.collection("all_roles")
                 .get()
                 .addOnSuccessListener { snapshot ->
                     val filteredRoles = snapshot.documents.mapNotNull { it.toObject(RoleDetails::class.java) }
                         .filter { it.doesMatchSearchQuery(query) }

                     onStateChange(UiState.Success(filteredRoles))
                 }
                 .addOnFailureListener { e ->
                     Log.e("SearchRoleVacancy", "Error fetching roles", e)
                     onStateChange(UiState.Error(e.message.toString()))
                 }
         }
         catch (exception : Exception){
             onStateChange(UiState.Error(exception.message.toString()))
         }
    }

    fun fetchVacancyFromFirebase(query: String , onStateChange  : (UiState<List<VacancyDetails>>) -> Unit){
        onStateChange(UiState.Loading)
        try {
            firebaseFirestore.collection("all_vacancy")
                .get()
                .addOnSuccessListener { snapshot ->
                    val filteredRoles = snapshot.documents.mapNotNull { it.toObject(VacancyDetails::class.java) }
                        .filter { it.doesMatchSearchQuery(query) }

                    onStateChange(UiState.Success(filteredRoles))
                }
                .addOnFailureListener { e ->
                    onStateChange(UiState.Error(e.message.toString()))
                }
        }
        catch (exception : Exception){
            onStateChange(UiState.Error(exception.message.toString()))
        }
    }
}