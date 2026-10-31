package com.hograna.liga.domain.usecase

import com.hograna.liga.data.local.entity.KnockoutTieEntity
import com.hograna.liga.data.repository.TournamentRepository
import javax.inject.Inject

sealed class KnockoutResult {
    data class WinnerDetermined(val winnerTeamId: Long) : KnockoutResult()
    data class NeedsPenalties(val tieId: Long, val teamAId: Long, val teamBId: Long) : KnockoutResult()
    object InsufficientData : KnockoutResult()
    data class Error(val cause: Throwable) : KnockoutResult()
}

/**
 * After both legs of a knockout tie are completed:
 * 1. Sums goals to get global aggregate.
 * 2. If drawn → returns NeedsPenalties.
 * 3. If winner → persists winnerTeamId and creates/updates the next-round slot.
 */
class AdvanceKnockoutTieUseCase @Inject constructor(
    private val repository: TournamentRepository
) {

    suspend fun invoke(tieId: Long): KnockoutResult {
        return try {
            val tie = repository.getTieById(tieId) ?: return KnockoutResult.InsufficientData
            val legs = repository.getMatchesByTieSuspend(tieId)
                .filter { !it.isBye && it.status == "COMPLETED" }

            if (legs.size < 2) return KnockoutResult.InsufficientData

            val teamAId = tie.teamAId ?: return KnockoutResult.InsufficientData
            val teamBId = tie.teamBId ?: return KnockoutResult.InsufficientData

            // Aggregate goals for each team across both legs
            var teamAGoals = 0
            var teamBGoals = 0
            legs.forEach { leg ->
                when {
                    leg.homeTeamId == teamAId -> {
                        teamAGoals += leg.homeGoals
                        teamBGoals += leg.awayGoals
                    }
                    leg.homeTeamId == teamBId -> {
                        teamBGoals += leg.homeGoals
                        teamAGoals += leg.awayGoals
                    }
                }
            }

            if (teamAGoals == teamBGoals) {
                return KnockoutResult.NeedsPenalties(tieId, teamAId, teamBId)
            }

            val winnerId = if (teamAGoals > teamBGoals) teamAId else teamBId
            advanceWinner(tie, winnerId)
            KnockoutResult.WinnerDetermined(winnerId)
        } catch (e: Exception) {
            KnockoutResult.Error(e)
        }
    }

    suspend fun invokePenalties(tieId: Long, penA: Int, penB: Int): KnockoutResult {
        if (penA < 0 || penB < 0 || penA == penB) {
            return KnockoutResult.Error(
                IllegalArgumentException("La tanda debe tener valores válidos y un ganador")
            )
        }
        return try {
            val tie = repository.getTieById(tieId) ?: return KnockoutResult.InsufficientData
            val teamAId = tie.teamAId ?: return KnockoutResult.InsufficientData
            val teamBId = tie.teamBId ?: return KnockoutResult.InsufficientData

            repository.setTiePenalties(tieId, penA, penB)
            val winnerId = if (penA > penB) teamAId else teamBId
            advanceWinner(tie.copy(teamAPenalties = penA, teamBPenalties = penB), winnerId)
            KnockoutResult.WinnerDetermined(winnerId)
        } catch (e: Exception) {
            KnockoutResult.Error(e)
        }
    }

    private suspend fun advanceWinner(tie: KnockoutTieEntity, winnerId: Long) {
        if (tie.winnerTeamId == winnerId) return
        val nextRound = tie.round / 2
        var nextTie: KnockoutTieEntity? = null
        var nextLegs = emptyList<com.hograna.liga.data.local.entity.MatchEntity>()
        if (nextRound >= 1) {
            val nextSlot = tie.slotIndex / 2
            val existingNextRound = repository.getTiesByRoundSuspend(nextRound)
            val slot = existingNextRound.firstOrNull { it.slotIndex == nextSlot }
                ?: throw IllegalStateException("No existe la llave de la siguiente ronda")
            nextTie = when {
                slot.teamAId == winnerId || slot.teamBId == winnerId -> slot
                slot.teamAId == null -> slot.copy(teamAId = winnerId)
                slot.teamBId == null -> slot.copy(teamBId = winnerId)
                else -> throw IllegalStateException("La llave de la siguiente ronda ya tiene dos equipos")
            }

            if (nextTie.teamAId != null && nextTie.teamBId != null) {
                val existingLegs = repository.getMatchesByTieSuspend(nextTie.id)
                if (existingLegs.isEmpty()) {
                    nextLegs = listOf(
                        com.hograna.liga.data.local.entity.MatchEntity(
                            stage = "KNOCKOUT",
                            matchdayOrRound = nextRound,
                            knockoutTieId = nextTie.id,
                            legNumber = 1,
                            homeTeamId = nextTie.teamAId,
                            awayTeamId = nextTie.teamBId
                        ),
                        com.hograna.liga.data.local.entity.MatchEntity(
                            stage = "KNOCKOUT",
                            matchdayOrRound = nextRound,
                            knockoutTieId = nextTie.id,
                            legNumber = 2,
                            homeTeamId = nextTie.teamBId,
                            awayTeamId = nextTie.teamAId
                        )
                    )
                }
            }
        }
        repository.advanceTie(tie.copy(winnerTeamId = winnerId), nextTie, nextLegs)
    }
}
