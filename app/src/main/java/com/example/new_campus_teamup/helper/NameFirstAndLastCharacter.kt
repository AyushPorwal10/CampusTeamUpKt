package com.example.new_campus_teamup.helper



object NameFirstAndLastCharacter{

    fun firstAndLastCharacter(fullName : String) : String {
        val names = fullName.trim().split("\\s+".toRegex())

        return when (names.size) {
            0 -> ""
            1 -> names[0].first().uppercase()
            else -> names[0].first().uppercase() + names.last().first().uppercase()
        }
    }
}