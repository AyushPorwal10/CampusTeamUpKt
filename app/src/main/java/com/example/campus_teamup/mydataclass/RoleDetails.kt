package com.example.campus_teamup.mydataclass

data class RoleDetails(
    val postedBy: String = "",
    val userName: String = "",
    val userImageUrl: String = "",
    val role: String = "",
    val postedOn: String = ""
){
    fun doesMatchSearchQuery(query: String): Boolean {
        return listOf(role, userName).any {
            it.contains(query, ignoreCase = true)
        }
    }
}