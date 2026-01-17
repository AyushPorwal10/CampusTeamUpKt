package com.example.new_campus_teamup.clean_code

import androidx.core.net.toUri
import com.example.new_campus_teamup.mydataclass.VacancyDetails
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class VacancyRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val storageReference: StorageReference,
) {
    suspend fun postTeamVacancy(
        details: VacancyDetails
    ) {
        try {
            val postId = generateVacancyId()
            val vacancyDetails = details.copy(postId = postId)

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

    suspend fun getVacancyPostedCount(userId: String) : Int{
        val snapshot =
            firebaseFirestore.collection("all_user_id")
                .document(userId)
                .collection("vacancy_posted")
                .get()
                .await()

      return snapshot.size()
    }

    suspend fun uploadTeamLogo(userId: String, teamLogoUri: String) : String{
       return  try {
            val imageRef = storageReference.child("team_logo/$userId/teamLogo.jpg")
            imageRef.putFile(teamLogoUri.toUri()).await()
            val downloadUrl = imageRef.downloadUrl.await()
           downloadUrl.toString()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun deleteVacancy(config: DeletePostConfig ){
        try{
            val batch = firebaseFirestore.batch()

            val allPostReference  = firebaseFirestore.collection("all_vacancy").document(config.postId)
            val userPostReference = firebaseFirestore.collection("all_user_id").document(config.userId).collection("vacancy_posted").document(config.postId)

            batch.delete(allPostReference)
            batch.delete(userPostReference)

            batch.commit().await()

        }
        catch (e : Exception){
            throw e
        }
    }



    private fun generateVacancyId() : String {
        return firebaseFirestore.collection("all_vacancy").document().id
    }

}