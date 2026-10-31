package com.hograna.liga.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hograna.liga.data.local.entity.KnockoutTieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KnockoutTieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTie(tie: KnockoutTieEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTies(ties: List<KnockoutTieEntity>): List<Long>

    @Update
    suspend fun updateTie(tie: KnockoutTieEntity)

    @Query("UPDATE knockout_ties SET winnerTeamId = :winnerId WHERE id = :tieId")
    suspend fun setWinner(tieId: Long, winnerId: Long)

    @Query("UPDATE knockout_ties SET teamAPenalties = :penA, teamBPenalties = :penB WHERE id = :tieId")
    suspend fun setPenalties(tieId: Long, penA: Int, penB: Int)

    @Query(
        """UPDATE knockout_ties SET 
           teamAId = CASE WHEN teamAId IS NULL THEN :teamId ELSE teamAId END,
           teamBId = CASE WHEN teamAId IS NOT NULL AND teamBId IS NULL THEN :teamId ELSE teamBId END
           WHERE id = :tieId"""
    )
    suspend fun assignTeamToTie(tieId: Long, teamId: Long)

    @Query("SELECT * FROM knockout_ties WHERE round = :round ORDER BY slotIndex ASC")
    fun getTiesByRound(round: Int): Flow<List<KnockoutTieEntity>>

    @Query("SELECT * FROM knockout_ties WHERE round = :round ORDER BY slotIndex ASC")
    suspend fun getTiesByRoundSuspend(round: Int): List<KnockoutTieEntity>

    @Query("SELECT * FROM knockout_ties ORDER BY round DESC, slotIndex ASC")
    fun getAllTies(): Flow<List<KnockoutTieEntity>>

    @Query("SELECT * FROM knockout_ties ORDER BY round DESC, slotIndex ASC")
    suspend fun getAllTiesSuspend(): List<KnockoutTieEntity>

    @Query("SELECT * FROM knockout_ties WHERE id = :id")
    suspend fun getTieById(id: Long): KnockoutTieEntity?

    @Delete
    suspend fun deleteTie(tie: KnockoutTieEntity)

    @Query("DELETE FROM knockout_ties")
    suspend fun clearAll()
}
