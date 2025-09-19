package com.example.new_campus_teamup.room

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey


@Keep
@Entity("RoleEntity")
data class RoleEntity(
    @PrimaryKey
    val roleId : String
)

@Keep
@Entity("VacancyEntity")
data class VacancyEntity(
    @PrimaryKey
    val vacancyId : String
)

@Keep
@Entity("ProjectEntity")
data class ProjectEntity(

    @PrimaryKey
    val projectId : String
)