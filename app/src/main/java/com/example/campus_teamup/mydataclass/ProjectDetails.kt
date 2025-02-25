package com.example.campus_teamup.mydataclass

data class ProjectDetails(
    val postedBy: String = "",
    val postedOn: String = "",
    val teamName: String = "",
    val hackathonOrPersonal: String = "",
    val problemStatement : String = "",
    val githubUrl : String = "",
    val projectLikes: Int = 0,
)