package com.example.new_campus_teamup.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow


@Dao
interface PostDao {


    // Roles
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRoleIds(roleId: List<RoleEntity>)

    @Query("Delete from RoleEntity where roleId NOT In (:roleIdsToKeep)")
    suspend fun deleteOldRoleIds(roleIdsToKeep: List<String>)

    @Query("Select * from RoleEntity")
    fun fetchRoleIds(): Flow<List<RoleEntity>>


    @Transaction
    suspend fun syncRoleIds(newRolesIds: List<RoleEntity>) {
        val idsToKeep = newRolesIds.map { it.roleId }
        deleteOldRoleIds(idsToKeep)
        saveRoleIds(newRolesIds)
    }


    // Vacancies
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveVacancyIds(roleId: List<VacancyEntity>)


    @Query("Delete from VacancyEntity where vacancyId NOT In (:vacancyIdsToKeep)")
   suspend fun deleteOldVacancyIds(vacancyIdsToKeep: List<String>)

    @Query("Select * from vacancyentity")
    fun fetchVacancyIds(): Flow<List<VacancyEntity>>


    @Transaction
    suspend fun syncVacancyIds(newVacancyIds: List<VacancyEntity>) {
        val idsToKeep = newVacancyIds.map { it.vacancyId }
        deleteOldVacancyIds(idsToKeep)
        saveVacancyIds(newVacancyIds)
    }


    // Projects
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProjectIds(roleId: List<ProjectEntity>)


    @Query("Select * from ProjectEntity")
    fun fetchProjectIds(): Flow<List<ProjectEntity>>

    @Query("Delete from ProjectEntity where projectId NOT In (:projectIdsToKeep)")
    suspend fun deleteOldProjectIds(projectIdsToKeep: List<String>)

    @Transaction
    suspend fun syncProjectIds(newProjectsIds: List<ProjectEntity>) {
        val idsToKeep = newProjectsIds.map { it.projectId }
        deleteOldProjectIds(idsToKeep)
        saveProjectIds(newProjectsIds)
    }
}