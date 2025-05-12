package com.example.new_campus_teamup.modules

import android.content.Context
import com.example.new_campus_teamup.notification.FCMApiService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object FirebaseObjects {


    @Singleton
    @Provides
    fun getFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun getFirebaseMessaging() : FirebaseMessaging{
        return FirebaseMessaging.getInstance()
    }

    @Singleton
    @Provides
    fun getRetrofit() : Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://us-central1-learnsign-in.cloudfunctions.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun getFCMApiService(retrofit: Retrofit) : FCMApiService{
        return retrofit.create(FCMApiService::class.java)
    }

    @Singleton
    @Provides
    fun getFirebaseFireStore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Singleton
    @Provides
    fun getFirebaseStorage(): StorageReference {
        return FirebaseStorage.getInstance().reference
    }


    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }




}