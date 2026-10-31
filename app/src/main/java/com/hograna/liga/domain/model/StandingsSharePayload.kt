package com.hograna.liga.domain.model

data class StandingsSharePayload(
    val tournamentTitle: String = "La liga Alfa Bravo",
    val topThreePodium: List<StandingWithStreakUiModel>,
    val topTenTable: List<StandingWithStreakUiModel>
)

data class StandingWithStreakUiModel(
    val position: Int,
    val teamId: Long,
    val teamName: String,
    val crestUri: String,
    val played: Int,
    val won: Int,
    val drawn: Int,
    val lost: Int,
    val points: Int,
    val lastFiveStreak: List<StreakBadgeType>
)
