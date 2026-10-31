package com.hograna.liga.data.repository

import com.hograna.liga.data.local.dao.KnockoutTieDao
import com.hograna.liga.data.local.dao.MatchDao
import com.hograna.liga.data.local.dao.TeamDao
import com.hograna.liga.data.local.dao.VideoHistoryDao
import com.hograna.liga.data.local.AppDatabase
import com.hograna.liga.data.local.entity.KnockoutTieEntity
import com.hograna.liga.data.local.entity.MatchEntity
import com.hograna.liga.data.local.entity.MatchPlayerEventEntity
import com.hograna.liga.data.local.entity.TeamEntity
import com.hograna.liga.data.local.entity.VideoHighlightEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.withTransaction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TournamentRepository @Inject constructor(
    private val teamDao: TeamDao,
    private val matchDao: MatchDao,
    private val knockoutTieDao: KnockoutTieDao,
    private val videoHistoryDao: VideoHistoryDao,
    private val database: AppDatabase
) {

    // ---- Teams ----

    fun getAllTeams(): Flow<List<TeamEntity>> = teamDao.getAllTeams()

    suspend fun getAllTeamsSuspend(): List<TeamEntity> = teamDao.getAllTeamsSuspend()

    suspend fun insertTeam(team: TeamEntity): Long = teamDao.insertTeam(team)

    suspend fun deleteTeam(team: TeamEntity) = teamDao.deleteTeam(team)

    suspend fun getTeamsOrderedByByeCount(): List<TeamEntity> =
        teamDao.getTeamsOrderedByByeCount()

    suspend fun incrementByeCount(teamId: Long) = teamDao.incrementByeCount(teamId)

    fun getTeamCount(): Flow<Int> = teamDao.getTeamCount()

    // ---- Matches ----

    fun getRegularMatches(): Flow<List<MatchEntity>> = matchDao.getRegularMatches()

    fun getKnockoutMatches(): Flow<List<MatchEntity>> = matchDao.getKnockoutMatches()

    fun getLatestMatchday(): Flow<Int?> = matchDao.getLatestMatchday()

    suspend fun insertMatch(match: MatchEntity): Long = matchDao.insertMatch(match)

    suspend fun insertMatches(matches: List<MatchEntity>) = matchDao.insertMatches(matches)

    suspend fun updateMatch(match: MatchEntity) = matchDao.updateMatch(match)

    suspend fun completeMatch(match: MatchEntity, events: List<MatchPlayerEventEntity>) =
        matchDao.completeMatch(match, events)

    suspend fun deleteMatchById(matchId: Long) = matchDao.deleteMatchById(matchId)

    suspend fun getMatchById(matchId: Long): MatchEntity? = matchDao.getMatchById(matchId)

    suspend fun getMatchesByMatchday(matchday: Int): List<MatchEntity> =
        matchDao.getMatchesByMatchdaySuspend(matchday)

    suspend fun insertMatchday(matches: List<MatchEntity>, byeTeamId: Long?) {
        database.withTransaction {
            matchDao.insertMatches(matches)
            byeTeamId?.let { teamDao.incrementByeCount(it) }
        }
    }

    suspend fun replacePlayerEvents(matchId: Long, events: List<MatchPlayerEventEntity>) =
        matchDao.replacePlayerEvents(matchId, events)

    fun getPlayerEventsForMatch(matchId: Long): Flow<List<MatchPlayerEventEntity>> =
        matchDao.getPlayerEventsForMatch(matchId)

    suspend fun getPlayerEventsForMatchSuspend(matchId: Long): List<MatchPlayerEventEntity> =
        matchDao.getPlayerEventsForMatchSuspend(matchId)

    // ---- Knockout ----

    fun getTiesByRound(round: Int): Flow<List<KnockoutTieEntity>> =
        knockoutTieDao.getTiesByRound(round)

    suspend fun getTiesByRoundSuspend(round: Int): List<KnockoutTieEntity> =
        knockoutTieDao.getTiesByRoundSuspend(round)

    suspend fun insertTie(tie: KnockoutTieEntity): Long = knockoutTieDao.insertTie(tie)

    suspend fun insertTies(ties: List<KnockoutTieEntity>) = knockoutTieDao.insertTies(ties)

    fun getAllTies(): Flow<List<KnockoutTieEntity>> = knockoutTieDao.getAllTies()

    suspend fun initializeBracket(
        ties: List<KnockoutTieEntity>,
        firstRoundTeams: List<Pair<Long, Long>>
    ) {
        database.withTransaction {
            check(knockoutTieDao.getAllTiesSuspend().isEmpty()) {
                "El bracket ya está iniciado"
            }
            val tieIds = knockoutTieDao.insertTies(ties)
            val firstRoundIndices = ties.indices.filter { ties[it].round == 8 }
            val matches = firstRoundIndices.flatMapIndexed { index, tieIndex ->
                val (teamA, teamB) = firstRoundTeams[index]
                val tieId = tieIds[tieIndex]
                listOf(
                    MatchEntity(
                        stage = "KNOCKOUT",
                        matchdayOrRound = 8,
                        knockoutTieId = tieId,
                        legNumber = 1,
                        homeTeamId = teamA,
                        awayTeamId = teamB
                    ),
                    MatchEntity(
                        stage = "KNOCKOUT",
                        matchdayOrRound = 8,
                        knockoutTieId = tieId,
                        legNumber = 2,
                        homeTeamId = teamB,
                        awayTeamId = teamA
                    )
                )
            }
            matchDao.insertMatches(matches)
        }
    }

    suspend fun getAllTiesSuspend(): List<KnockoutTieEntity> =
        knockoutTieDao.getAllTiesSuspend()

    suspend fun updateTie(tie: KnockoutTieEntity) {
        knockoutTieDao.updateTie(tie)
    }

    suspend fun advanceTie(
        completedTie: KnockoutTieEntity,
        nextTie: KnockoutTieEntity?,
        nextLegs: List<MatchEntity>
    ) {
        database.withTransaction {
            knockoutTieDao.updateTie(completedTie)
            nextTie?.let { knockoutTieDao.updateTie(it) }
            if (nextLegs.isNotEmpty()) matchDao.insertMatches(nextLegs)
        }
    }

    suspend fun setTieWinner(tieId: Long, winnerId: Long) =
        knockoutTieDao.setWinner(tieId, winnerId)

    suspend fun setTiePenalties(tieId: Long, penA: Int, penB: Int) =
        knockoutTieDao.setPenalties(tieId, penA, penB)

    suspend fun getTieById(id: Long): KnockoutTieEntity? = knockoutTieDao.getTieById(id)

    fun getMatchesByTie(tieId: Long): Flow<List<MatchEntity>> =
        matchDao.getMatchesByTie(tieId)

    suspend fun getMatchesByTieSuspend(tieId: Long): List<MatchEntity> =
        matchDao.getMatchesByTieSuspend(tieId)

    // ---- Videos ----

    fun getAllVideos(): Flow<List<VideoHighlightEntity>> = videoHistoryDao.getAllVideos()

    suspend fun insertVideo(video: VideoHighlightEntity): Long =
        videoHistoryDao.insertVideo(video)

    suspend fun deleteVideo(video: VideoHighlightEntity) = videoHistoryDao.deleteVideo(video)
}
