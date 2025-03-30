package com.example.campus_teamup.mydataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class VacancyDetails(
    var vacancyId : String = "",
    val postedBy: String = "",
    val postedOn: String = "",
    val teamLogo: String = "",
    val teamName: String = "",
    val hackathonName: String = "",
    val roleLookingFor: String = "",
    val skills: String = "",
    val roleDescription: String = ""
) : Parcelable {
    fun doesMatchSearchQuery(query: String): Boolean {
        return listOf(roleLookingFor, teamName,hackathonName).any {
            it.contains(query, ignoreCase = true)
        }
    }
}