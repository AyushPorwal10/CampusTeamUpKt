package com.example.new_campus_teamup.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("RoleEntity")
data class RoleEntity(
    @PrimaryKey
    val roleId : String
)

@Entity("VacancyEntity")
data class VacancyEntity(
    @PrimaryKey
    val vacancyId : String
)

@Entity("ProjectEntity")
data class ProjectEntity(

    @PrimaryKey
    val projectId : String
)