package com.example.campus_teamup.helper

import com.google.firebase.auth.ActionCodeSettings
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActionCodeSettingsProvider @Inject constructor() {

    fun getActionCodeSettings(): ActionCodeSettings {
        return ActionCodeSettings.newBuilder()
            .setUrl("https://learnsign-in.firebaseapp.com")
            .setHandleCodeInApp(true)
            .setAndroidPackageName("com.example.campus_teamup", false, "11")
            .build()
    }
}