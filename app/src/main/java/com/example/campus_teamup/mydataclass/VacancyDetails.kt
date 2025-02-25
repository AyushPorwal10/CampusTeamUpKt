package com.example.campus_teamup.mydataclass

data class VacancyDetails(
    val postedBy: String = "",
    val postedOn: String = "",
    val teamLogo: String = "",
    val teamName: String = "",
    val hackathonName: String = "",
    val roleLookingFor: String = "",
    val skills: String = "",
    val roleDescription: String = ""
){
    fun doesMatchSearchQuery(query: String): Boolean {
        return listOf(roleLookingFor, teamName,hackathonName).any {
            it.contains(query, ignoreCase = true)
        }
    }
}