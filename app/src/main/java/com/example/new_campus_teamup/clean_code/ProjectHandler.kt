package com.example.new_campus_teamup.clean_code

import com.example.new_campus_teamup.mydataclass.ProjectDetails
import com.example.new_campus_teamup.viewmodels.DeletePostResult
import com.example.new_campus_teamup.viewmodels.PostResult
import javax.inject.Inject

class ProjectHandler @Inject constructor(
    private val projectRepository: ProjectRepository
): BasePostHandler {


    override suspend fun post(postDto: BasePostDto): PostResult {

        val projectDetails = postDto as? ProjectDetails
            ?: return PostResult.Failure("Invalid project data")

        return try {
            projectRepository.postProject(projectDetails)
            PostResult.Success
        } catch (e: Exception) {
            PostResult.Failure("Something went wrong")
        }
    }


    override suspend fun delete(config: DeletePostConfig): DeletePostResult {
        return try {
            projectRepository.deleteProject(config)
            DeletePostResult.PostDeleted
        }
        catch (exception : Exception){
            DeletePostResult.Failure("Something went wrong")
        }
    }

}