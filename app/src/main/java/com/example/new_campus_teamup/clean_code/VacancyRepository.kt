package com.example.new_campus_teamup.clean_code

import com.example.new_campus_teamup.mydataclass.VacancyDetails
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class VacancyRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val storageReference: StorageReference
) {
    suspend fun postTeamVacancy(
        vacancyDetails: VacancyDetails
    ) {
        try {
            val postId = generateVacancyId()
            vacancyDetails.postId = postId

            firebaseFirestore.runTransaction { transaction ->

                val toUserDetails = firebaseFirestore
                    .collection("all_user_id")
                    .document(vacancyDetails.postedBy)
                    .collection("vacancy_posted")
                    .document(postId)

                val toAllVacancy = firebaseFirestore
                    .collection("all_vacancy")
                    .document(postId)

                transaction.set(toUserDetails, vacancyDetails)
                transaction.set(toAllVacancy, vacancyDetails)
            }.await()
        } catch (e: Exception) {
            throw e
        }
    }


    private fun generateVacancyId() : String {
        return firebaseFirestore.collection("all_vacancy").document().id
    }

}