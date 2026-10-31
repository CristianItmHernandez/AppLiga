package com.hograna.liga.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hograna.liga.data.local.entity.TeamEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTeam(team: TeamEntity): Long

    @Update
    suspend fun updateTeam(team: TeamEntity)

    @Delete
    suspend fun deleteTeam(team: TeamEntity)

    @Query("SELECT * FROM teams ORDER BY name ASC")
    fun getAllTeams(): Flow<List<TeamEntity>>

    @Query("SELECT * FROM teams ORDER BY name ASC")
    suspend fun getAllTeamsSuspend(): List<TeamEntity>

    @Query("SELECT * FROM teams WHERE id = :id")
    suspend fun getTeamById(id: Long): TeamEntity?

    @Query("SELECT * FROM teams WHERE id IN (:ids)")
    suspend fun getTeamsByIds(ids: List<Long>): List<TeamEntity>

    @Query("SELECT COUNT(*) FROM teams")
    fun getTeamCount(): Flow<Int>

    @Query("UPDATE teams SET byeCount = byeCount + 1 WHERE id = :teamId")
    suspend fun incrementByeCount(teamId: Long)

    @Query("SELECT * FROM teams ORDER BY byeCount ASC, name ASC")
    suspend fun getTeamsOrderedByByeCount(): List<TeamEntity>
}
