package com.hograna.liga.domain.usecase

import com.hograna.liga.data.local.entity.MatchEntity
import com.hograna.liga.data.local.entity.MatchPlayerEventEntity
import com.hograna.liga.data.repository.TournamentRepository
import javax.inject.Inject

data class CompleteMatchInput(
    val matchId: Long,
    val homeGoals: Int,
    val awayGoals: Int,
    val homeFouls: Int,
    val awayFouls: Int,
    val mvpPlayerName: String?,
    val mvpRating: Double?,
    val playerEvents: List<MatchPlayerEventEntity>
)

/**
 * Atomically marks a match as COMPLETED, saves goals/fouls/MVP, and replaces player events.
 * Room's reactive Flows will automatically push updates to StandingsDao and StatsDao.
 */
class CompleteMatchUseCase @Inject constructor(
    private val repository: TournamentRepository
) {

    sealed class Result {
        object Success : Result()
        object MatchNotFound : Result()
        data class Error(val cause: Throwable) : Result()
    }

    suspend fun invoke(input: CompleteMatchInput): Result {
        return try {
            val existing = repository.getMatchById(input.matchId)
                ?: return Result.MatchNotFound
            if (
                input.homeGoals < 0 ||
                input.awayGoals < 0 ||
                input.homeFouls < 0 ||
                input.awayFouls < 0 ||
                input.playerEvents.any { it.count <= 0 }
            ) {
                return Result.Error(IllegalArgumentException("Los goles, faltas y eventos deben ser válidos"))
            }
            if (existing.isBye || existing.awayTeamId == null) {
                return Result.Error(IllegalStateException("Un partido de descanso no se puede completar"))
            }
            val participantIds = setOf(existing.homeTeamId, existing.awayTeamId)
            if (input.playerEvents.any { it.teamId !in participantIds }) {
                return Result.Error(IllegalArgumentException("Cada evento debe pertenecer a uno de los equipos del partido"))
            }
            if (
                input.mvpRating != null &&
                (input.mvpRating !in 1.0..10.0 || input.mvpPlayerName.isNullOrBlank())
            ) {
                return Result.Error(IllegalArgumentException("La calificación MVP debe estar entre 1.0 y 10.0 y tener jugador"))
            }
            if ((input.mvpPlayerName.isNullOrBlank()) != (input.mvpRating == null)) {
                return Result.Error(IllegalArgumentException("El jugador MVP y su calificación deben guardarse juntos"))
            }
            if (existing.knockoutTieId != null) {
                val tie = repository.getTieById(existing.knockoutTieId)
                if (tie?.winnerTeamId != null) {
                    return Result.Error(
                        IllegalStateException("No se puede editar un partido después de avanzar su llave")
                    )
                }
            }

            val updated = existing.copy(
                status = "COMPLETED",
                homeGoals = input.homeGoals,
                awayGoals = input.awayGoals,
                homeFouls = input.homeFouls,
                awayFouls = input.awayFouls,
                mvpPlayerName = input.mvpPlayerName,
                mvpRating = input.mvpRating
            )

            repository.completeMatch(updated, input.playerEvents)

            Result.Success
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
