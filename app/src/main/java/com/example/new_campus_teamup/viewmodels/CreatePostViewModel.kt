package com.example.new_campus_teamup.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.new_campus_teamup.helper.CheckNetworkConnectivity
import com.example.new_campus_teamup.myactivities.UserManager
import com.example.new_campus_teamup.mydataclass.CollegeDetails
import com.example.new_campus_teamup.mydataclass.VacancyDetails
import com.example.new_campus_teamup.myrepository.CreatePostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val userManager: UserManager,
    private val createPostRepository: CreatePostRepository,
    private val networkMonitor: CheckNetworkConnectivity
) : ViewModel() {


    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage


    private lateinit var userId: String
    private lateinit var userName: String
    private lateinit var phoneNumber: String
    private lateinit var collegeName : String
    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    private fun launchWithLoading(block : suspend  () -> Unit){
        viewModelScope.launch {
            _isLoading.value = true
            if (!networkMonitor.isConnectedNow()) {
                _errorMessage.value = "No internet connection. Please retry later."
                return@launch
            }
            try {
                block()
            } catch (toe: TimeoutCancellationException) {
                _errorMessage.value = "Request timed out. Check your connection."
            } catch (e: Exception) {
                Log.e("HomeScreenVM", "Unexpected error", e)
                _errorMessage.value = "Something went wrong. Please try again."
            } finally {
                _isLoading.value = false
            }
        }
    }
    init {
        launchWithLoading {
            fetchDataFromDataStore()
        }
    }
    fun clearError() {
        _errorMessage.value = null
    }

    private suspend fun fetchDataFromDataStore() {

        Log.d("PostRole", "Fetching of data from datastore started")
        val userData = userManager.userData.first()
        userId = userData.userId
        userName = userData.userName
        phoneNumber = userData.phoneNumber
        collegeName = userData.collegeName

        Log.d("PostRole", "Updated User Id: $userId")
    }

    fun postRole(role: String, datePosted: String, canPostRole: (Boolean) -> Unit) {

        launchWithLoading {
            val snapshot = createPostRepository.fetchImageUrlFromUserDetails(userId)
            var userImageUrl = snapshot.toObject(CollegeDetails::class.java)
            createPostRepository.postRole(
                collegeName,
                userId,
                userName,
                userImageUrl?.userImageUrl ?: "",
                role,
                datePosted, canPostRole = {
                    _isLoading.value = false
                    canPostRole(it)
                }
            )
        }

    }

    fun uploadTeamLogo(teamLogoUri: String, onResult: (Boolean, String?) -> Unit) {
        launchWithLoading{
            createPostRepository.uploadTeamLogo(
                userId,
                teamLogoUri,
                canPostVacancy = { canPost, url ->
                    onResult(canPost, url)
                })
        }
    }


    fun postVacancy(
        postedOn: String,
        teamLogo: String,
        teamName: String,
        hackathonName: String,
        roleLookingFor: String,
        skill: String,
        roleDescription: String,
        onVacancyPosted: () -> Unit
    ) {

        launchWithLoading {

            Log.d("Vacancy", "Going to post vacancy")


                createPostRepository.postTeamVacancy(
                    userId, VacancyDetails(
                        "",   // this will be generated in repo class
                        userId,
                        postedOn,
                        collegeName,
                        teamLogo,
                        teamName,
                        hackathonName,
                        roleLookingFor,
                        skill,
                        roleDescription,
                        "123456"
                    )
                )
            onVacancyPosted()
        }
    }

    fun postProject(
        postedOn: String,
        teamName: String,
        hackathonOrPersonal: String,
        problemStatement: String,
        githubUrl: String,
        projectLikes: Int,
        onProjectPosted : () -> Unit ,
    ) {


        launchWithLoading {

            createPostRepository.addProject(
                userId,
                postedOn,
                teamName,
                hackathonOrPersonal,
                problemStatement,
                githubUrl,
                projectLikes
            )
            onProjectPosted ()
        }
    }
}