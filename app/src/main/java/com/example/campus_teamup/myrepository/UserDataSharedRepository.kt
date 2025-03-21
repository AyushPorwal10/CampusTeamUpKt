package com.example.campus_teamup.myrepository

import com.example.campus_teamup.myactivities.UserData
import com.example.campus_teamup.myactivities.UserManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserDataSharedRepository @Inject constructor(
    private val userManager: UserManager
) {

    fun fetchUserDataFromDataStore() : Flow<UserData> {
        return userManager.userData
    }
}