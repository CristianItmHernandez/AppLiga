package com.hograna.liga.domain.usecase

import com.hograna.liga.data.local.entity.MatchEntity
import com.hograna.liga.data.repository.TournamentRepository
import javax.inject.Inject

/**
 * Generates a new matchday with fair bye rotation for odd-team counts.
 *
 * Rules:
 * - If teams count is even: random shuffle + sequential pairing.
 * - If teams count is odd: the team with lowest byeCount (random tie-break) gets the bye
 *   and has its byeCount incremented atomically. The rest are paired randomly.
 * - All matches are inserted in a single bulk operation.
 */
class GenerateMatchdayUseCase @Inject constructor(
    private val repository: TournamentRepository
) {

    sealed class Result {
        data class Success(val matches: List<MatchEntity>) : Result()
        object NotEnoughTeams : Result()
        data class Error(val cause: Throwable) : Result()
    }

    suspend fun invoke(matchday: Int): Result {
        return try {
            val teams = repository.getTeamsOrderedByByeCount()

            if (teams.size < 2) return Result.NotEnoughTeams
            if (repository.getMatchesByMatchday(matchday).isNotEmpty()) {
                return Result.Error(IllegalStateException("La jornada $matchday ya fue generada"))
            }

            val shuffled = teams.shuffled().toMutableList()
            val matches = mutableListOf<MatchEntity>()
            var byeTeamId: Long? = null

            if (shuffled.size % 2 != 0) {
                // Odd number: find the group with the minimum byeCount
                val minBye = shuffled.minOf { it.byeCount }
                val candidates = shuffled.filter { it.byeCount == minBye }
                val byeTeam = candidates.random()
                shuffled.remove(byeTeam)

                // Register the bye match (isBye=true, awayTeamId=null)
                matches.add(
                    MatchEntity(
                        stage = "REGULAR",
                        matchdayOrRound = matchday,
                        homeTeamId = byeTeam.id,
                        awayTeamId = null,
                        isBye = true,
                        status = "COMPLETED"  // Byes are auto-completed
                    )
                )
                byeTeamId = byeTeam.id
            }

            // Pair remaining teams sequentially
            for (i in shuffled.indices step 2) {
                if (i + 1 < shuffled.size) {
                    matches.add(
                        MatchEntity(
                            stage = "REGULAR",
                            matchdayOrRound = matchday,
                            homeTeamId = shuffled[i].id,
                            awayTeamId = shuffled[i + 1].id,
                            isBye = false,
                            status = "PENDING"
                        )
                    )
                }
            }

            repository.insertMatchday(matches, byeTeamId)
            Result.Success(matches)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
