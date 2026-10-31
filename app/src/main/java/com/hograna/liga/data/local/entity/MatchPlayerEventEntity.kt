package com.hograna.liga.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "match_player_events",
    foreignKeys = [
        ForeignKey(
            entity = MatchEntity::class,
            parentColumns = ["id"],
            childColumns = ["matchId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TeamEntity::class,
            parentColumns = ["id"],
            childColumns = ["teamId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("matchId"),
        Index("teamId"),
        Index("eventType")
    ]
)
data class MatchPlayerEventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val matchId: Long,
    val teamId: Long,
    val playerName: String,
    /** GOAL | ASSIST | SAVE | YELLOW_CARD | RED_CARD */
    val eventType: String,
    val count: Int = 1
)
