package com.example.new_campus_teamup.clean_code_1

import com.example.new_campus_teamup.myrepository.SavedItemsRepository
import javax.inject.Inject

class ViewRoleHandler @Inject constructor(
    private val savedItemsRepository: SavedItemsRepository
): ViewPostHandler {
    override suspend fun savePost(config: SaveUnsaveConfig): ViewPostResult {
        return ViewPostResult.Saved
    }

    override suspend fun deleteSavedPost(config: SaveUnsaveConfig): ViewPostResult {
        if(config.userId == null) return ViewPostResult.Failure

        return try {
            savedItemsRepository.deleteSavedRole(config)
            ViewPostResult.Unsaved
        }
        catch (e : Exception){
            ViewPostResult.Failure
        }
    }

    override suspend fun reportPost(): ViewPostResult {
        return ViewPostResult.Saved
    }

}