package com.example.campus_teamup.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campus_teamup.myactivities.UserManager
import com.example.campus_teamup.mydataclass.CollegeDetails
import com.example.campus_teamup.mydataclass.ProjectDetails
import com.example.campus_teamup.mydataclass.RoleDetails
import com.example.campus_teamup.mydataclass.VacancyDetails
import com.example.campus_teamup.myrepository.CreatePostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val userManager: UserManager,
    private val createPostRepository: CreatePostRepository
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
            _isLoading.value = true;
            try{
                block()
            }
            catch (e : Exception){
                Log.e("CreatePostViewModel", "Error: ${e.message}", e)
                _errorMessage.value = "An unexpected error occurred"
            }
            finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    suspend fun fetchDataFromDataStore() {

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
            val snapshot = createPostRepository.fetchImageUrlFromUserDetails(phoneNumber)
            var userImageUrl = snapshot.toObject(CollegeDetails::class.java)
            createPostRepository.postRole(
                collegeName,
                phoneNumber,
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
                phoneNumber,
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
                    phoneNumber, VacancyDetails(
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
                        phoneNumber
                    )
                )
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
                phoneNumber,
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