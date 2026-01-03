package com.example.new_campus_teamup.clean_code

import androidx.compose.ui.geometry.Rect
import com.example.new_campus_teamup.mydataclass.RoleDetails
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
            roleRepository.postRole(roleDetails)
            PostResult.Success
        } catch (e: Exception) {
            PostResult.Failure("Something went wrong")
        }
    }


    override suspend fun delete(config: DeletePostConfig): Boolean {
        return true
    }


}


