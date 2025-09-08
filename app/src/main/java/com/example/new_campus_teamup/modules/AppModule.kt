package com.example.new_campus_teamup.modules

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.new_campus_teamup.room.AppDatabase
import com.example.new_campus_teamup.room.PostDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesRoomDatabase(@ApplicationContext context : Context) : AppDatabase{
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "campus_teamup_offline"
        ).build()
    }

    @Singleton
    @Provides
    fun providesPostDao(appDatabase: AppDatabase) : PostDao{
        return appDatabase.postDao()
    }
}