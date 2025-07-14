package com.example.new_campus_teamup.mydataclass

import androidx.annotation.Keep

@Keep
data class EducationDetails(val userName : String = "",
                            val collegeName : String = "",
                            val year : String = "",
                            val course : String = "",
                            val branch : String = "")