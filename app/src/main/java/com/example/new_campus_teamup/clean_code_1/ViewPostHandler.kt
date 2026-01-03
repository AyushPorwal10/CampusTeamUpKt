package com.example.new_campus_teamup.clean_code_1

interface ViewPostHandler {
    suspend fun savePost(config: SaveUnsaveConfig) : ViewPostResult
    suspend fun deleteSavedPost(config: SaveUnsaveConfig) : ViewPostResult
    suspend fun reportPost() : ViewPostResult
}

sealed class ViewPostResult {
    data object Saved : ViewPostResult()
    data object Unsaved : ViewPostResult()
    data object Reported : ViewPostResult()
    data object Failure : ViewPostResult()
}

data class SaveUnsaveConfig(
    val userId : String? ,
    val postId : String
)