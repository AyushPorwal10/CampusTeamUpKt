package com.example.new_campus_teamup.clean_code

import com.example.new_campus_teamup.mydataclass.ProjectDetails
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProjectRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val storageReference: StorageReference
){
  suspend  fun postProject(
        projectDetails: ProjectDetails
    )  {
         try {
            val batch = firebaseFirestore.batch()
            projectDetails.postId = generateProjectId()

            val addProject =
                firebaseFirestore.collection("all_projects")
                    .document(projectDetails.postId)

            val updateProjectPostedList = firebaseFirestore
                .collection("all_user_id")
                .document(projectDetails.postedBy)
                .collection("project_posted")
                .document(projectDetails.postId)

            batch.set(updateProjectPostedList, projectDetails)

            batch.set(addProject, projectDetails)

            batch.commit().await()
        }
        catch (e : Exception){
            throw e
        }
    }

    private fun generateProjectId() : String {
        return firebaseFirestore.collection("all_projects").document().id
    }

}