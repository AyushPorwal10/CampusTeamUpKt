package com.example.new_campus_teamup.clean_code

import android.util.Log
import com.example.new_campus_teamup.mydataclass.RoleDetails
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RoleHandler @Inject constructor(
    private val roleRepository: RoleRepository
): BasePostHandler {

    override suspend fun post(postDto: BasePostDto): Boolean {
        if(roleRepository.getPostedRoleCount(postDto.postedBy) == 3) return false

        val roleDetails = postDto as RoleDetails
        roleRepository.postRole(
            roleDetails = roleDetails,
        )
        return true
    }

    override suspend fun delete(config: DeletePostConfig): Boolean {
        return true
    }


}


