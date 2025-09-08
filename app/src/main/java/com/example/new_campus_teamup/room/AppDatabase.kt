package com.example.new_campus_teamup.room

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [RoleEntity::class, VacancyEntity::class, ProjectEntity::class], version = 1 , exportSchema = true)
abstract class AppDatabase : RoomDatabase(){
    abstract fun postDao() : PostDao
}