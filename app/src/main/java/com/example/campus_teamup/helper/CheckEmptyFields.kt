package com.example.campus_teamup.helper

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList

object CheckEmptyFields {

    fun checkEmail(
        email: String
    ): String {

        return if (email.isEmpty())
            "Empty Email"
        else if (!isValidEmail(email))
            return "Invalid Email Format"
        else
            ""
    }

    fun checkName(name: String): String {
        if (name.isEmpty())
            return "Empty Name"
        return ""
    }

    fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")

        return emailRegex.matches(email)
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
    ) : Boolean{

        if(teamName.trim().isEmpty() || hackathonName.trim().isEmpty() || roleLookingFor.trim().isEmpty() || skills.trim().isEmpty())
            return false;
        return true
    }


    fun checkProjectFields(
        teamName: String,
        hackathonName: String,
        problemStatement : String
    ) : Boolean{

        if(teamName.trim().isEmpty() || hackathonName.trim().isEmpty() || problemStatement.trim().isEmpty())
            return false;
        return true
    }

    fun checkTeamMemberUserNameIsEmpty(teamMembersUserName: List<String>): Boolean{
        teamMembersUserName.forEach{
            if(it.isEmpty())
                return true
        }
        return false
    }

    fun isUserNameInPresent(teamMembersUserName: List<String> , userName : String): Boolean{
        Log.d("TeamDetailsUserId","isUserNamePresent = $userName")
        teamMembersUserName.forEach{
            if(it == userName)
                return true
        }
        return false
    }
}
