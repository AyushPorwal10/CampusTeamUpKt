package com.example.new_campus_teamup.mydataclass

import androidx.annotation.Keep
import com.example.new_campus_teamup.clean_code.BasePostDto

@Keep
data class ProjectDetails(
    override val postId: String = "",
    override val postedBy: String = "",
    override val postedOn: String = "",

    val projectId: String = "",
    val teamName: String = "",
    val hackathonOrPersonal: String = "",
    val problemStatement: String = "",
    val githubUrl: String = "",
    val projectLikes: Int = 0,
) : BasePostDto