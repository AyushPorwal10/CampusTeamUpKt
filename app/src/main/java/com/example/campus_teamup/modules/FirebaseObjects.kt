package com.example.campus_teamup.modules

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object FirebaseObjects {


    @Singleton
    @Provides
    fun getFirebaseAuth() : FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
    @Singleton
    @Provides
    fun getFirebaseFireStore() : FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Singleton
    @Provides
    fun getFirebaseStorage() : StorageReference {
        return FirebaseStorage.getInstance().reference
    }
    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context{
     return context
    }
}