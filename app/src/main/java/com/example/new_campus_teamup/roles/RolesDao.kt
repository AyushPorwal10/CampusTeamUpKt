package com.example.new_campus_teamup.roles

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.new_campus_teamup.mydataclass.RoleDetails
import kotlinx.coroutines.flow.Flow


@Dao
interface RolesDao {


    @Query("Delete from role_details")
    suspend fun clearRoles()


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoles(roles: List<RoleDetails>)

    @Query("Select * from role_details ORDER by postedOn DESC")
    fun getRoles(): Flow<List<RoleDetails>>


    @Transaction
    suspend fun syncRoles(roles : List<RoleDetails>) {
        clearRoles()
        insertRoles(roles = roles)
    }
}