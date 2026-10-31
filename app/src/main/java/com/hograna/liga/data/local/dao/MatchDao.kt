package com.hograna.liga.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.hograna.liga.data.local.entity.MatchEntity
import com.hograna.liga.data.local.entity.MatchPlayerEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatch(match: MatchEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatches(matches: List<MatchEntity>)

    @Update
    suspend fun updateMatch(match: MatchEntity)

    @Delete
    suspend fun deleteMatch(match: MatchEntity)

    @Query("DELETE FROM matches WHERE id = :matchId")
    suspend fun deleteMatchById(matchId: Long)

    @Query("SELECT * FROM matches WHERE stage = 'REGULAR' ORDER BY matchdayOrRound ASC, id ASC")
    fun getRegularMatches(): Flow<List<MatchEntity>>

    @Query("SELECT * FROM matches WHERE stage = 'KNOCKOUT' ORDER BY matchdayOrRound ASC, id ASC")
    fun getKnockoutMatches(): Flow<List<MatchEntity>>

    @Query("SELECT * FROM matches WHERE id = :matchId")
    suspend fun getMatchById(matchId: Long): MatchEntity?

    @Query(
        """SELECT * FROM matches 
           WHERE stage = 'REGULAR' AND matchdayOrRound = :matchday 
           ORDER BY id ASC"""
    )
    fun getMatchesByMatchday(matchday: Int): Flow<List<MatchEntity>>

    @Query(
        """SELECT * FROM matches
           WHERE stage = 'REGULAR' AND matchdayOrRound = :matchday
           ORDER BY id ASC"""
    )
    suspend fun getMatchesByMatchdaySuspend(matchday: Int): List<MatchEntity>

    @Query("SELECT MAX(matchdayOrRound) FROM matches WHERE stage = 'REGULAR'")
    fun getLatestMatchday(): Flow<Int?>

    @Query(
        """SELECT * FROM matches 
           WHERE knockoutTieId = :tieId 
           ORDER BY legNumber ASC"""
    )
    fun getMatchesByTie(tieId: Long): Flow<List<MatchEntity>>

    @Query(
        """SELECT * FROM matches 
           WHERE knockoutTieId = :tieId 
           ORDER BY legNumber ASC"""
    )
    suspend fun getMatchesByTieSuspend(tieId: Long): List<MatchEntity>

    // Player events

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayerEvents(events: List<MatchPlayerEventEntity>)

    @Query("DELETE FROM match_player_events WHERE matchId = :matchId")
    suspend fun deletePlayerEventsForMatch(matchId: Long)

    @Query("SELECT * FROM match_player_events WHERE matchId = :matchId")
    fun getPlayerEventsForMatch(matchId: Long): Flow<List<MatchPlayerEventEntity>>

    @Query("SELECT * FROM match_player_events WHERE matchId = :matchId")
    suspend fun getPlayerEventsForMatchSuspend(matchId: Long): List<MatchPlayerEventEntity>

    @Transaction
    suspend fun replacePlayerEvents(matchId: Long, events: List<MatchPlayerEventEntity>) {
        deletePlayerEventsForMatch(matchId)
        if (events.isNotEmpty()) insertPlayerEvents(events)
    }

    @Transaction
    suspend fun completeMatch(match: MatchEntity, events: List<MatchPlayerEventEntity>) {
        updateMatch(match)
        replacePlayerEvents(match.id, events)
    }
}
