package com.hograna.liga.domain.model

/**
 * Projection returned by [StatsDao] queries for scorers, assisters, saves, and cards.
 */
data class StatRow(
    val playerName: String,
    val teamName: String,
    val crestUri: String,
    val total: Int
)
