package com.hograna.liga.domain.usecase

import com.hograna.liga.data.local.entity.KnockoutTieEntity
import com.hograna.liga.data.repository.TournamentRepository
import javax.inject.Inject

sealed class InitializeBracketResult {
    object Success : InitializeBracketResult()
    object NotEnoughTeams : InitializeBracketResult()
    object AlreadyInitialized : InitializeBracketResult()
    data class InvalidSelection(val reason: String) : InitializeBracketResult()
    data class Error(val cause: Throwable) : InitializeBracketResult()
}

class InitializeKnockoutBracketUseCase @Inject constructor(
    private val repository: TournamentRepository
) {
    suspend fun invoke(teamIds: List<Long>): InitializeBracketResult {
        if (teamIds.size < 16) return InitializeBracketResult.NotEnoughTeams
        if (teamIds.size != 16 || teamIds.distinct().size != 16) {
            return InitializeBracketResult.InvalidSelection("Selecciona 16 equipos distintos para los octavos")
        }

        return try {
            if (repository.getAllTiesSuspend().isNotEmpty()) {
                return InitializeBracketResult.AlreadyInitialized
            }
            val knownTeamIds = repository.getAllTeamsSuspend().map { it.id }.toSet()
            if (!knownTeamIds.containsAll(teamIds)) {
                return InitializeBracketResult.InvalidSelection("La selección contiene equipos no registrados")
            }

            val ties = buildList {
                listOf(8, 4, 2, 1).forEach { round ->
                    repeat(round) { slot ->
                        val firstRound = round == 8
                        add(
                            KnockoutTieEntity(
                                round = round,
                                slotIndex = slot,
                                teamAId = if (firstRound) teamIds[slot * 2] else null,
                                teamBId = if (firstRound) teamIds[slot * 2 + 1] else null
                            )
                        )
                    }
                }
            }
            val firstRoundTeams = ties
                .filter { it.round == 8 }
                .map { requireNotNull(it.teamAId) to requireNotNull(it.teamBId) }
            repository.initializeBracket(ties, firstRoundTeams)
            InitializeBracketResult.Success
        } catch (error: Exception) {
            InitializeBracketResult.Error(error)
        }
    }
}
