package com.hograna.liga.domain.usecase

import com.hograna.liga.data.local.entity.MatchEntity
import com.hograna.liga.data.repository.TournamentRepository
import javax.inject.Inject

data class ManualMatchInput(
    val matchId: Long? = null,
    val matchday: Int,
    val homeTeamId: Long,
    val awayTeamId: Long
)

sealed class ManualMatchResult {
    data class Saved(val match: MatchEntity) : ManualMatchResult()
    object TeamNotFound : ManualMatchResult()
    object MatchNotFound : ManualMatchResult()
    data class Invalid(val reason: String) : ManualMatchResult()
    data class Error(val cause: Throwable) : ManualMatchResult()
}

class UpsertManualMatchUseCase @Inject constructor(
    private val repository: TournamentRepository
) {
    suspend fun invoke(input: ManualMatchInput): ManualMatchResult {
        if (input.matchday < 1) return ManualMatchResult.Invalid("La jornada debe ser mayor que cero")
        if (input.homeTeamId == input.awayTeamId) {
            return ManualMatchResult.Invalid("Un equipo no puede enfrentarse a sí mismo")
        }

        return try {
            val teams = repository.getAllTeamsSuspend()
            if (teams.none { it.id == input.homeTeamId } || teams.none { it.id == input.awayTeamId }) {
                return ManualMatchResult.TeamNotFound
            }

            val existing = input.matchId?.let { repository.getMatchById(it) }
            if (input.matchId != null && existing == null) return ManualMatchResult.MatchNotFound
            if (existing != null && (existing.stage != "REGULAR" || existing.isBye)) {
                return ManualMatchResult.Invalid("Solo se pueden reasignar partidos regulares")
            }
            if (existing?.status == "COMPLETED") {
                return ManualMatchResult.Invalid("No se puede reasignar un partido completado")
            }

            val conflictingMatch = repository.getMatchesByMatchday(input.matchday).any { match ->
                match.id != input.matchId &&
                    (match.homeTeamId == input.homeTeamId ||
                        match.awayTeamId == input.homeTeamId ||
                        match.homeTeamId == input.awayTeamId ||
                        match.awayTeamId == input.awayTeamId)
            }
            if (conflictingMatch) {
                return ManualMatchResult.Invalid("Cada equipo solo puede jugar una vez por jornada")
            }

            val match = existing?.copy(
                matchdayOrRound = input.matchday,
                homeTeamId = input.homeTeamId,
                awayTeamId = input.awayTeamId
            ) ?: MatchEntity(
                stage = "REGULAR",
                matchdayOrRound = input.matchday,
                homeTeamId = input.homeTeamId,
                awayTeamId = input.awayTeamId
            )

            if (existing == null) repository.insertMatch(match) else repository.updateMatch(match)
            ManualMatchResult.Saved(match)
        } catch (error: Exception) {
            ManualMatchResult.Error(error)
        }
    }
}
