package com.example.campus_teamup.viewmodels

import android.net.Uri
import android.util.Log
import androidx.compose.ui.semantics.Role
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campus_teamup.helper.show
import com.example.campus_teamup.myactivities.UserManager
import com.example.campus_teamup.mydataclass.CollegeDetails
import com.example.campus_teamup.mydataclass.ProjectDetails
import com.example.campus_teamup.mydataclass.RoleDetails
import com.example.campus_teamup.mydataclass.VacancyDetails
import com.example.campus_teamup.myrepository.CreatePostRepository
import com.example.campus_teamup.screens.LoginScreen
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val userManager: UserManager,
    private val createPostRepository: CreatePostRepository
) : ViewModel() {

    private lateinit var userId: String
    private lateinit var userName: String
    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    var _isPosted = MutableStateFlow<Boolean>(false)
    var isPosted: StateFlow<Boolean> = _isPosted


    suspend fun fetchDataFromDataStore() {

        Log.d("PostRole", "Fetching of data from datastore started")
        val userData = userManager.userData.first()
        userId = userData.userId
        userName = userData.userName

        Log.d("PostRole", "Updated User Id: $userId")
    }

    fun postRole(role: String, datePosted: String) {

        viewModelScope.launch {
            _isLoading.value = true
            withContext(Dispatchers.IO) {
                val snapshot = createPostRepository.fetchImageUrlFromUserDetails(userId)
                var userImageUrl = snapshot.toObject(CollegeDetails::class.java)

                createPostRepository.postRole(
                    userId, RoleDetails(
                        userId,
                        userName,
                        userImageUrl?.userImageUrl ?: "",
                        role,
                        datePosted
                    )
                )
            }
            _isPosted.value = true
            _isLoading.value = false

            delay(2000)
            _isPosted.value = false

        }
    }

    fun uploadTeamLogo(teamLogoUri: Uri, onResult: (String?) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true

            val result = withContext(Dispatchers.IO) {
                createPostRepository.uploadTeamLogo(userId, teamLogoUri)
            }

            _isLoading.value = false
            onResult(result)
        }
    }


    fun postVacancy(
        postedOn: String,
        teamLogo: String,
        teamName: String,
        hackathonName: String,
        roleLookingFor: String,
        skill: String,
        roleDescription: String
    ) {

        viewModelScope.launch {
            _isLoading.value = true
            Log.d("Vacancy", "Going to post vacancy")
            withContext(Dispatchers.IO) {
                createPostRepository.postVacancy(
                    userId, VacancyDetails(
                        userId,
                        postedOn,
                        teamLogo,
                        teamName,
                        hackathonName,
                        roleLookingFor,
                        skill,
                        roleDescription
                    )
                )
            }

            _isLoading.value = false
            _isPosted.value = true

            delay(2000)
            _isPosted.value = false

        }
    }

    fun postProject(
        postedOn: String,
        teamName: String,
        hackathonOrPersonal: String,
        problemStatement : String ,
        githubUrl: String,
        projectLikes: Int
    ) {


        viewModelScope.launch {
            _isLoading.value = true

            withContext(Dispatchers.IO){
                createPostRepository.postProject(userId ,
                    ProjectDetails(
                        userId,
                        postedOn,
                        teamName,
                        hackathonOrPersonal,
                        problemStatement,
                        githubUrl,
                        projectLikes
                    )
                )
            }

            _isLoading.value  = false
            _isPosted.value = true
        }
    }
}