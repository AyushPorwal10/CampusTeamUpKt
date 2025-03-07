package com.example.campus_teamup.helper

import com.google.firebase.auth.ActionCodeSettings
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActionCodeSettingsProvider @Inject constructor() {

    fun getActionCodeSettings(): ActionCodeSettings {
        return ActionCodeSettings.newBuilder()
            .setUrl("https://learnsign-in.web.app")
            .setHandleCodeInApp(true)
            .setAndroidPackageName("com.example.campus_teamup", true, "11")
            .build()
    }
}