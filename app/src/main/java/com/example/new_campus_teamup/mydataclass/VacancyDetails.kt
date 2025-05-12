package com.example.new_campus_teamup.mydataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

import androidx.annotation.Keep


@Keep
@Parcelize
data class VacancyDetails(
    var vacancyId : String = "",
    val postedBy: String = "",
    val postedOn: String = "",
    val collegeName : String = "",
    val teamLogo: String = "",
    val teamName: String = "",
    val hackathonName: String = "",
    val roleLookingFor: String = "",
    val skills: String = "",
    val roleDescription: String = "",
    val phoneNumber : String = "",
) : Parcelable {
    fun doesMatchSearchQuery(query: String): Boolean {
        return listOf(roleLookingFor, teamName,hackathonName,skills,collegeName).any {
            it.contains(query, ignoreCase = true)
        }
    }
}