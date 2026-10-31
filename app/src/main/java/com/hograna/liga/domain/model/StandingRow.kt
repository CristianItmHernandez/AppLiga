package com.hograna.liga.domain.model

/**
 * Projection returned by [StandingsDao.getStandings].
 * Used directly as a Room query result (no @Entity annotation needed).
 */
data class StandingRow(
    val teamId: Long,
    val teamName: String,
    val crestUri: String,
    val captainName: String,
    val played: Int,
    val won: Int,
    val drawn: Int,
    val lost: Int,
    val goalsFor: Int,
    val goalsAgainst: Int,
    val points: Int
) {
    val goalDifference: Int get() = goalsFor - goalsAgainst
}
