package com.example.campus_teamup.helper

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList

object CheckEmptyFields {


    fun isValidHttpsUrl(url: String?): Boolean {
        if (url.isNullOrBlank()) return false
        return try {
            val uri = Uri.parse(url)
            uri.scheme == "https" && !uri.host.isNullOrEmpty()
        } catch (e: Exception) {
            false
        }
    }


    fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")

        return emailRegex.matches(email)
    }

    fun isCodingProfileUrlAreValid(listOfProfiles: List<String>): Boolean {

        for (url in listOfProfiles) {
            if (url.trim().isEmpty() || url.isBlank()) return false
            try {
                val uri = Uri.parse(url)
                if (uri.scheme != "https" || uri.host.isNullOrEmpty()) return false
            } catch (e: Exception) {
                return false
            }
        }
        return true
    }


    fun checkCodingProfiles(listOfProfiles: List<String>): Boolean {
        listOfProfiles.forEach { profile ->
            if (profile.isEmpty() || !profile.contains(".com"))
                return false
        }
        return true
    }

    fun checkVacancyFields(
        teamName: String,
        hackathonName: String,
        roleLookingFor: String,
        skills: String
    ): Boolean {

        if (teamName.trim().isEmpty() || hackathonName.trim().isEmpty() || roleLookingFor.trim()
                .isEmpty() || skills.trim().isEmpty()
        )
            return false;
        return true
    }


    fun checkProjectFields(
        teamName: String,
        hackathonName: String,
        problemStatement: String
    ): Boolean {

        if (teamName.trim().isEmpty() || hackathonName.trim().isEmpty() || problemStatement.trim()
                .isEmpty()
        )
            return false;
        return true
    }

    fun checkTeamMemberUserNameIsEmpty(teamMembersUserName: List<String>): Boolean {
        teamMembersUserName.forEach {
            if (it.isEmpty())
                return true
        }
        return false
    }

    fun isUserNameInPresent(teamMembersUserName: List<String>, userName: String): Boolean {
        Log.d("TeamDetailsUserId", "isUserNamePresent = $userName")
        teamMembersUserName.forEach {
            if (it == userName)
                return true
        }
        return false
    }

    fun isSignUpDetailsAreCorrect(
        email: String,
        collegeName: String,
        name: String,
        phoneNumber: String
    ): Boolean {

        return name.isNotEmpty() && collegeName.isNotEmpty() && phoneNumber.length == 10  && isValidEmail(
            email
        )
    }

}
