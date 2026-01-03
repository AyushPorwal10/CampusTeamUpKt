package com.example.new_campus_teamup.mydataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

import androidx.annotation.Keep
import com.example.new_campus_teamup.clean_code.BasePostDto


@Keep
@Parcelize
data class VacancyDetails(
    override var postId: String = "",
    override val postedOn: String = "",
    override val postedBy: String = "",

    var vacancyId : String = "",
    val collegeName : String = "",
    val teamLogo: String = "",
    val teamName: String = "",
    val hackathonName: String = "",
    val roleLookingFor: String = "",
    val skills: String = "",
    val roleDescription: String = "",
    val phoneNumber : String = "",
) : BasePostDto, Parcelable {
    fun doesMatchSearchQuery(query: String): Boolean {
        return listOf(roleLookingFor, teamName,hackathonName,skills,collegeName).any {
            it.contains(query, ignoreCase = true)
        }
    }
}