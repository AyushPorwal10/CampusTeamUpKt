package com.example.new_campus_teamup.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.new_campus_teamup.mydataclass.RoleDetails
import com.example.new_campus_teamup.roles.RolesDao


@Database(entities = [RoleEntity::class, VacancyEntity::class, ProjectEntity::class, RoleDetails::class], version = 1 , exportSchema = true)
abstract class AppDatabase : RoomDatabase(){
    abstract fun postDao() : PostDao
    abstract fun rolesDao() : RolesDao
}