package com.example.new_campus_teamup.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.new_campus_teamup.clean_code.PostHandlerFactory
import com.example.new_campus_teamup.clean_code.PostType
import com.example.new_campus_teamup.helper.CheckNetworkConnectivity
import com.example.new_campus_teamup.myactivities.UserManager
import com.example.new_campus_teamup.mydataclass.ProjectDetails
import com.example.new_campus_teamup.mydataclass.RoleDetails
import com.example.new_campus_teamup.mydataclass.VacancyDetails
import com.example.new_campus_teamup.myrepository.CreatePostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val userManager: UserManager,
    private val createPostRepository: CreatePostRepository,
    private val networkMonitor: CheckNetworkConnectivity,

    private val postHandlerFactory: PostHandlerFactory
) : ViewModel() {


    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _postUiEvent = MutableSharedFlow<String>()
    val postUiEvent = _postUiEvent.asSharedFlow()

    private lateinit var userId: String
    private lateinit var userName: String
    private lateinit var phoneNumber: String
    private lateinit var collegeName: String
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    private fun launchWithLoading(block: suspend () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            if (!networkMonitor.isConnectedNow()) {
                _errorMessage.value = "No internet connection. Please retry later."
                return@launch
            }
            try {
                block()
            } catch (_: TimeoutCancellationException) {
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

    fun postRole(role: String, datePosted: String) {

        launchWithLoading {
            val snapshot = createPostRepository.fetchImageUrlFromUserDetails(userId)
            val userImageUrl = snapshot.get("user_image")

            val result = postHandlerFactory.getHandler(PostType.ROLE)
                .post(
                    RoleDetails(
                        collegeName = collegeName,
                        postedBy = userId,
                        userName = userName,
                        userImageUrl = userImageUrl?.toString() ?: "",
                        role = role,
                        postedOn = datePosted
                    )
                )
            when (result) {
                is PostResult.Success -> _postUiEvent.emit("Role Posted Successfully")
                is PostResult.PostLimitReached ->  _postUiEvent.emit("You can post only 3 roles")
                is PostResult.Failure ->  _postUiEvent.emit("Something went wrong\nPlease try again later!")
            }
        }
    }

    fun uploadTeamLogo(teamLogoUri: String, onResult: (Boolean, String?) -> Unit) {
        launchWithLoading {
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
    ) {

        launchWithLoading {

            Log.d("Vacancy", "Going to post vacancy")

            val result = postHandlerFactory.getHandler(PostType.VACANCY).post(
                postDto = VacancyDetails(
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

            when (result) {
                is PostResult.Success -> _postUiEvent.emit("Vacancy Posted Successfully")
                is PostResult.PostLimitReached ->  _postUiEvent.emit("You can post only 4 Vacancies")
                is PostResult.Failure ->  _postUiEvent.emit("Something went wrong\nPlease try again later!")
            }
        }
    }

    fun postProject(
        postedOn: String,
        teamName: String,
        hackathonOrPersonal: String,
        problemStatement: String,
        githubUrl: String,
        projectLikes: Int,
    ) {


        launchWithLoading {
            val result = postHandlerFactory.getHandler(PostType.PROJECT).post(
                postDto = ProjectDetails(
                    postedBy = userId,
                    postedOn = postedOn,
                    teamName = teamName,
                    hackathonOrPersonal = hackathonOrPersonal,
                    problemStatement = problemStatement,
                    githubUrl = githubUrl,
                    projectLikes = projectLikes
                )
            )
            when (result) {
                is PostResult.Success -> _postUiEvent.emit("Project Posted Successfully")
                is PostResult.Failure ->  _postUiEvent.emit("Something went wrong\nPlease try again later!")
                else -> Unit // no limit
            }
        }
    }
}

sealed class PostResult {
    data object Success : PostResult()
    data object PostLimitReached : PostResult()
    data class Failure(val reason: String) : PostResult()
}
