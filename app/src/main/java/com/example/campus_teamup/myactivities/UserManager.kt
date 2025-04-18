package com.example.campus_teamup.myactivities

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserManager @Inject constructor(private val context: Context) {

    companion object {
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        private val USER_COLLEGE_KEY = stringPreferencesKey("user_college")
        private val LOGIN_OR_SIGNUP_KEY = stringPreferencesKey("login_or_signup")
        private val FCM_TOKEN_KEY = stringPreferencesKey("fcm_token")
        private val PHONE_NUMBER = stringPreferencesKey("phone_number")
    }

    suspend fun saveUserData(userId: String, userName : String , email: String, college: String ,phoneNumber : String ,  loginOrSignUp : String) {
        context.dataStore.edit { preferences ->
            Log.d("Signup","User data saved $userId , $userName ,$email , $college , $loginOrSignUp" )
            preferences[USER_ID_KEY] = userId
            preferences[USER_NAME_KEY] = userName
            preferences[USER_EMAIL_KEY] = email
            preferences[USER_COLLEGE_KEY] = college
            preferences[LOGIN_OR_SIGNUP_KEY] = loginOrSignUp
            preferences[PHONE_NUMBER] = phoneNumber
        }

    }

    suspend fun saveUserFCM(fcmToken : String){
        context.dataStore.edit {preferences->
            preferences[FCM_TOKEN_KEY] = fcmToken
        }
    }
    val userData: Flow<UserData> = context.dataStore.data.map { preferences ->
        UserData(
            userId = preferences[USER_ID_KEY] ?: "",
            userName = preferences[USER_NAME_KEY] ?: "",
            email = preferences[USER_EMAIL_KEY] ?: "",
            collegeName = preferences[USER_COLLEGE_KEY] ?: "",
            loginOrSignUp = preferences[LOGIN_OR_SIGNUP_KEY] ?: "",
            phoneNumber = preferences[PHONE_NUMBER] ?: ""
        )
    }

    val fcmToken : Flow<String> = context.dataStore.data.map {
        it[FCM_TOKEN_KEY] ?: ""
    }

    suspend fun clearUserData(){
        Log.d("Userdata" , "Clearing User Data")
        context.dataStore.edit { it.clear() }
    }
}

data class UserData(val userId: String = "",
                    val userName: String = "",
                    val email: String = "",
                    val collegeName: String = "",
                    val loginOrSignUp: String = "",
                    val phoneNumber: String = "")
