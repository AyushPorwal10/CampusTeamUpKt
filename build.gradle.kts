buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.2")
        classpath("com.android.tools.build:gradle:8.11.1")
        classpath("com.google.firebase:firebase-crashlytics-gradle:3.0.3")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.11.1" apply false
    id("org.jetbrains.kotlin.android") version "2.2.0" apply false
    id("com.google.dagger.hilt.android") version "2.57" apply false
    id("com.google.firebase.crashlytics") version "3.0.3" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.0" apply false

}

