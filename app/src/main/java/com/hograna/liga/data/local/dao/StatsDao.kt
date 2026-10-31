package com.hograna.liga.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.hograna.liga.domain.model.StatRow
import kotlinx.coroutines.flow.Flow

@Dao
interface StatsDao {

    @Query(
        """
        SELECT mpe.playerName, t.name AS teamName, t.crestUri, SUM(mpe.count) AS total
        FROM match_player_events mpe
        JOIN teams t ON mpe.teamId = t.id
        JOIN matches m ON mpe.matchId = m.id
        WHERE mpe.eventType = 'GOAL' AND m.status = 'COMPLETED'
        GROUP BY mpe.playerName, mpe.teamId
        ORDER BY total DESC, mpe.playerName ASC
        """
    )
    fun getTopScorers(): Flow<List<StatRow>>

    @Query(
        """
        SELECT mpe.playerName, t.name AS teamName, t.crestUri, SUM(mpe.count) AS total
        FROM match_player_events mpe
        JOIN teams t ON mpe.teamId = t.id
        JOIN matches m ON mpe.matchId = m.id
        WHERE mpe.eventType = 'ASSIST' AND m.status = 'COMPLETED'
        GROUP BY mpe.playerName, mpe.teamId
        ORDER BY total DESC, mpe.playerName ASC
        """
    )
    fun getTopAssisters(): Flow<List<StatRow>>

    @Query(
        """
        SELECT mpe.playerName, t.name AS teamName, t.crestUri, SUM(mpe.count) AS total
        FROM match_player_events mpe
        JOIN teams t ON mpe.teamId = t.id
        JOIN matches m ON mpe.matchId = m.id
        WHERE mpe.eventType = 'SAVE' AND m.status = 'COMPLETED'
        GROUP BY mpe.playerName, mpe.teamId
        ORDER BY total DESC, mpe.playerName ASC
        """
    )
    fun getTopSavers(): Flow<List<StatRow>>

    @Query(
        """
        SELECT mpe.playerName, t.name AS teamName, t.crestUri, SUM(mpe.count) AS total
        FROM match_player_events mpe
        JOIN teams t ON mpe.teamId = t.id
        JOIN matches m ON mpe.matchId = m.id
        WHERE mpe.eventType = 'YELLOW_CARD' AND m.status = 'COMPLETED'
        GROUP BY mpe.playerName, mpe.teamId
        ORDER BY total DESC, mpe.playerName ASC
        """
    )
    fun getYellowCards(): Flow<List<StatRow>>

    @Query(
        """
        SELECT mpe.playerName, t.name AS teamName, t.crestUri, SUM(mpe.count) AS total
        FROM match_player_events mpe
        JOIN teams t ON mpe.teamId = t.id
        JOIN matches m ON mpe.matchId = m.id
        WHERE mpe.eventType = 'RED_CARD' AND m.status = 'COMPLETED'
        GROUP BY mpe.playerName, mpe.teamId
        ORDER BY total DESC, mpe.playerName ASC
        """
    )
    fun getRedCards(): Flow<List<StatRow>>
}
