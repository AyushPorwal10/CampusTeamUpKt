package com.example.new_campus_teamup.clean_code_1

import com.example.new_campus_teamup.clean_code.BasePostDto
import com.example.new_campus_teamup.clean_code.PostType

interface ViewPostHandler {
    suspend fun savePost(config: SavePostConfig) : ViewPostResult
    suspend fun deleteSavedPost(config: DeletePostConfig) : ViewPostResult
    suspend fun reportPost(config: RepostPostConfig) : ViewPostResult
}

sealed class ViewPostResult {
    data object Saved : ViewPostResult()
    data object Unsaved : ViewPostResult()
    data object Reported : ViewPostResult()
    data object Failure : ViewPostResult()
}

data class DeletePostConfig(
    val userId : String? ,
    val postId : String
)
data class SavePostConfig(
    val postDto : BasePostDto,
    val savedBy : String // The user who is going to save role
)
data class RepostPostConfig(
    val postType: PostType,
    val postId : String,
    val reportedBy : String // id of user who reported post
)