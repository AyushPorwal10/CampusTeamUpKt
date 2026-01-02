package com.example.new_campus_teamup.mydataclass

import androidx.annotation.Keep
import com.example.new_campus_teamup.clean_code.BasePostDto

@Keep
data class RoleDetails(
    override val postId: String = "",
    override val postedBy: String = "",
    override val postedOn: String = "",

    val phoneNumber: String = "",
    val collegeName: String = "",
    val roleId: String = "",
    val userName: String = "",
    val userImageUrl: String = "",
    val role: String = "",
) : BasePostDto {
    fun doesMatchSearchQuery(query: String): Boolean {
        return listOf(role, userName, collegeName).any {
            it.contains(query, ignoreCase = true)
        }
    }
}