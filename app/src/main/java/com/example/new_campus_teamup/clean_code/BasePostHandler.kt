package com.example.new_campus_teamup.clean_code

interface BasePostHandler {
    suspend fun post(postDto: BasePostDto): Boolean

    suspend fun delete(config: DeletePostConfig): Boolean
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

