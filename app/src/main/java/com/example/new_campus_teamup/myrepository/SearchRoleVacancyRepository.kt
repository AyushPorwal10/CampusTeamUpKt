package com.example.new_campus_teamup.myrepository

import android.util.Log
import com.example.new_campus_teamup.mydataclass.RoleDetails
import com.example.new_campus_teamup.mydataclass.VacancyDetails
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class SearchRoleVacancyRepository @Inject constructor(
    val firebaseFirestore: FirebaseFirestore,
) {

     fun fetchRolesFromFirebase(query: String , onSearched : (List<RoleDetails>) -> Unit , onError : (Exception) -> Unit){

        firebaseFirestore.collection("all_roles")
            .get()
            .addOnSuccessListener { snapshot ->
                val filteredRoles = snapshot.documents.mapNotNull { it.toObject(RoleDetails::class.java) }
                    .filter { it.doesMatchSearchQuery(query) }

                onSearched(filteredRoles)
            }
            .addOnFailureListener { e ->
                Log.e("SearchRoleVacancy", "Error fetching roles", e)
               onError(e)
            }
    }

    fun fetchVacancyFromFirebase(query: String , onSearched : (List<VacancyDetails>) -> Unit , onError : (Exception) -> Unit){

        firebaseFirestore.collection("all_vacancy")
            .get()
            .addOnSuccessListener { snapshot ->
                val filteredRoles = snapshot.documents.mapNotNull { it.toObject(VacancyDetails::class.java) }
                    .filter { it.doesMatchSearchQuery(query) }

                onSearched(filteredRoles)
            }
            .addOnFailureListener { e ->
                Log.e("SearchRoleVacancy", "Error fetching roles", e)
                onError(e)
            }
    }
}