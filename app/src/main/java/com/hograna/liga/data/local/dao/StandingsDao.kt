package com.hograna.liga.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.hograna.liga.domain.model.StandingRow
import kotlinx.coroutines.flow.Flow

@Dao
interface StandingsDao {

    @Query(
        """
        SELECT 
            t.id AS teamId,
            t.name AS teamName,
            t.crestUri AS crestUri,
            t.captainName AS captainName,
            COUNT(m.id) AS played,
            SUM(CASE 
                WHEN (m.homeTeamId = t.id AND m.homeGoals > m.awayGoals) OR 
                     (m.awayTeamId = t.id AND m.awayGoals > m.homeGoals) THEN 1 ELSE 0 END) AS won,
            SUM(CASE 
                WHEN m.homeGoals = m.awayGoals THEN 1 ELSE 0 END) AS drawn,
            SUM(CASE 
                WHEN (m.homeTeamId = t.id AND m.homeGoals < m.awayGoals) OR 
                     (m.awayTeamId = t.id AND m.awayGoals < m.homeGoals) THEN 1 ELSE 0 END) AS lost,
            SUM(CASE 
                WHEN m.homeTeamId = t.id THEN m.homeGoals 
                WHEN m.awayTeamId = t.id THEN m.awayGoals
                ELSE 0 END) AS goalsFor,
            SUM(CASE 
                WHEN m.homeTeamId = t.id THEN m.awayGoals
                WHEN m.awayTeamId = t.id THEN m.homeGoals
                ELSE 0 END) AS goalsAgainst,
            SUM(CASE 
                WHEN (m.homeTeamId = t.id AND m.homeGoals > m.awayGoals) OR 
                     (m.awayTeamId = t.id AND m.awayGoals > m.homeGoals) THEN 3
                WHEN m.homeGoals = m.awayGoals THEN 1 
                ELSE 0 END) AS points
        FROM teams t
        LEFT JOIN matches m ON (m.homeTeamId = t.id OR m.awayTeamId = t.id) 
            AND m.stage = 'REGULAR' AND m.status = 'COMPLETED' AND m.isBye = 0
        GROUP BY t.id
        ORDER BY points DESC, 
                 (SUM(CASE WHEN m.homeTeamId = t.id THEN m.homeGoals ELSE m.awayGoals END) -
                  SUM(CASE WHEN m.homeTeamId = t.id THEN m.awayGoals ELSE m.homeGoals END)) DESC,
                 goalsFor DESC,
                 t.name ASC
        """
    )
    fun getStandings(): Flow<List<StandingRow>>
}
