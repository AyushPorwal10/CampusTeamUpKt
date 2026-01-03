package com.example.new_campus_teamup.clean_code

import com.example.new_campus_teamup.mydataclass.RoleDetails
import com.example.new_campus_teamup.viewmodels.DeletePostResult
import com.example.new_campus_teamup.viewmodels.PostResult
import javax.inject.Inject

class RoleHandler @Inject constructor(
    private val roleRepository: RoleRepository
): BasePostHandler {

    override suspend fun post(postDto: BasePostDto): PostResult {
        val userId = postDto.postedBy

        if (roleRepository.getPostedRoleCount(userId) >= 3) {
            return PostResult.PostLimitReached
        }

        val roleDetails = postDto as? RoleDetails
            ?: return PostResult.Failure("Invalid role data")

        return try {
            val userImageUrl = roleRepository.fetchImageUrlFromUserDetails(userId)

            roleRepository.postRole(roleDetails.copy(userImageUrl = userImageUrl))
            PostResult.Success
        } catch (e: Exception) {
            PostResult.Failure("Something went wrong")
        }
    }


    override suspend fun delete(config: DeletePostConfig): DeletePostResult {
        return try {
            roleRepository.deleteRole(config)
            DeletePostResult.PostDeleted
        }
        catch (exception : Exception){
            DeletePostResult.Failure("Something went wrong")
        }
    }

}


