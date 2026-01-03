package com.example.new_campus_teamup.clean_code

import com.example.new_campus_teamup.mydataclass.VacancyDetails
import com.example.new_campus_teamup.viewmodels.PostResult
import javax.inject.Inject

class VacancyHandler @Inject constructor(
    private val vacancyRepository: VacancyRepository
) : BasePostHandler {

    override suspend fun post(postDto: BasePostDto): PostResult {
        val vacancyDetails = postDto as? VacancyDetails
            ?: return PostResult.Failure("Invalid vacancy data")

        return try {
            vacancyRepository.postTeamVacancy(vacancyDetails)
            PostResult.Success
        } catch (e: Exception) {
            PostResult.Failure("Something went wrong")
        }
    }


    override suspend fun delete(config: DeletePostConfig): Boolean {
        return true
    }

}