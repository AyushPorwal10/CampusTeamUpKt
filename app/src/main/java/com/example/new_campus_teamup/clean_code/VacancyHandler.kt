package com.example.new_campus_teamup.clean_code

import com.example.new_campus_teamup.mydataclass.VacancyDetails
import com.example.new_campus_teamup.viewmodels.DeletePostResult
import com.example.new_campus_teamup.viewmodels.PostResult
import javax.inject.Inject

class VacancyHandler @Inject constructor(
    private val vacancyRepository: VacancyRepository
) : BasePostHandler {

    override suspend fun post(postDto: BasePostDto): PostResult {

        if(vacancyRepository.getVacancyPostedCount(postDto.postedBy) >= 4){
            return PostResult.PostLimitReached
        }

        val vacancyDetails = postDto as? VacancyDetails
            ?: return PostResult.Failure("Invalid vacancy data")

        return try {
            // getting url from uri after uploading to firebase
            val teamLogo = vacancyRepository.uploadTeamLogo(userId = postDto.postedBy, teamLogoUri = postDto.teamLogo)

            vacancyRepository.postTeamVacancy(vacancyDetails.copy(teamLogo = teamLogo))
            PostResult.Success
        } catch (e: Exception) {
            PostResult.Failure("Something went wrong")
        }
    }


    override suspend fun delete(config: DeletePostConfig): DeletePostResult {
        return try {
            vacancyRepository.deleteVacancy(config)
            DeletePostResult.PostDeleted
        }
        catch (exception : Exception){
            DeletePostResult.Failure("Something went wrong")
        }
    }

}