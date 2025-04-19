package com.example.campus_teamup.mydataclass

data class RoleDetails(
    val phoneNumber : String = "",
    val collegeName : String = "",
    val roleId : String = "",
    val postedBy: String = "",
    val userName: String = "",
    val userImageUrl: String = "",
    val role: String = "",
    val postedOn: String = ""
){
    fun doesMatchSearchQuery(query: String): Boolean {
        return listOf(role, userName , collegeName).any {
            it.contains(query, ignoreCase = true)
        }
    }
}