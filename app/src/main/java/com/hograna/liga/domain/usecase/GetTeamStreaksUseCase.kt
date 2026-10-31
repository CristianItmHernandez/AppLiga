package com.hograna.liga.domain.usecase

import com.hograna.liga.data.local.entity.MatchEntity
import com.hograna.liga.domain.model.StreakBadgeType
import javax.inject.Inject

class GetTeamStreaksUseCase @Inject constructor() {

    operator fun invoke(matches: List<MatchEntity>): Map<Long, List<StreakBadgeType>> {
        val streaks = mutableMapOf<Long, MutableList<StreakBadgeType>>()

        matches.asSequence()
            .filter {
                it.stage == "REGULAR" &&
                    it.status == "COMPLETED" &&
                    !it.isBye &&
                    it.awayTeamId != null
            }
            .sortedWith(compareBy<MatchEntity> { it.matchdayOrRound }.thenBy { it.id })
            .forEach { match ->
                val awayTeamId = requireNotNull(match.awayTeamId)
                val resultForHome = when {
                    match.homeGoals > match.awayGoals -> StreakBadgeType.WIN
                    match.homeGoals < match.awayGoals -> StreakBadgeType.LOSS
                    else -> StreakBadgeType.DRAW
                }
                val resultForAway = when (resultForHome) {
                    StreakBadgeType.WIN -> StreakBadgeType.LOSS
                    StreakBadgeType.DRAW -> StreakBadgeType.DRAW
                    StreakBadgeType.LOSS -> StreakBadgeType.WIN
                }
                streaks.getOrPut(match.homeTeamId) { mutableListOf() }.add(resultForHome)
                streaks.getOrPut(awayTeamId) { mutableListOf() }.add(resultForAway)
            }

        return streaks
    }
}
