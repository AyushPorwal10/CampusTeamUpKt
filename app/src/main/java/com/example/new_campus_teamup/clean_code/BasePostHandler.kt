package com.example.new_campus_teamup.clean_code

import com.example.new_campus_teamup.viewmodels.DeletePostResult
import com.example.new_campus_teamup.viewmodels.PostResult

interface BasePostHandler {
    suspend fun post(postDto: BasePostDto): PostResult

    suspend fun delete(config: DeletePostConfig): DeletePostResult
}


interface BasePostDto {
    val postId: String
    val postedOn: String
    val postedBy: String
}

data class DeletePostConfig(
    val userId: String,
    val postId: String
)

