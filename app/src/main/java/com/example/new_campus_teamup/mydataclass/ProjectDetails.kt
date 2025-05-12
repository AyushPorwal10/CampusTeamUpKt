package com.example.new_campus_teamup.mydataclass

import androidx.annotation.Keep

@Keep
data class ProjectDetails(
    val projectId : String = "",
    val postedBy: String = "",
    val postedOn: String = "",
    val teamName: String = "",
    val hackathonOrPersonal: String = "",
    val problemStatement : String = "",
    val githubUrl : String = "",
    val projectLikes: Int = 0,
)