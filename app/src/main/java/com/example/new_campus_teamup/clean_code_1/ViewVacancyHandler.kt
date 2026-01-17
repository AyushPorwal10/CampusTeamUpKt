package com.example.new_campus_teamup.clean_code_1

import com.example.new_campus_teamup.myrepository.HomeScreenRepository
import com.example.new_campus_teamup.myrepository.SavedItemsRepository
import javax.inject.Inject

class ViewVacancyHandler @Inject constructor(
    private val savedItemsRepository: SavedItemsRepository,
    private val homeScreenRepository: HomeScreenRepository
): ViewPostHandler {
    override suspend fun savePost(config: SavePostConfig): ViewPostResult {
        return try {
            homeScreenRepository.saveVacancy(config)
            ViewPostResult.Saved
        }
        catch (exception : Exception){
            ViewPostResult.Failure
        }
    }

    override suspend fun deleteSavedPost(config: DeletePostConfig): ViewPostResult {
        if(config.userId == null) return ViewPostResult.Failure

        return try {
            savedItemsRepository.deleteSavedVacancy(config)
            ViewPostResult.Unsaved
        }
        catch (e : Exception){
            ViewPostResult.Failure
        }
    }

    override suspend fun reportPost(config: RepostPostConfig): ViewPostResult {
        return try {
            homeScreenRepository.reportPost(config)
            ViewPostResult.Reported
        }
        catch (exception : Exception){
            ViewPostResult.Failure
        }
    }

}