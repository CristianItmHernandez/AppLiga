package com.hograna.liga.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "matches",
    foreignKeys = [
        ForeignKey(
            entity = TeamEntity::class,
            parentColumns = ["id"],
            childColumns = ["homeTeamId"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = TeamEntity::class,
            parentColumns = ["id"],
            childColumns = ["awayTeamId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = KnockoutTieEntity::class,
            parentColumns = ["id"],
            childColumns = ["knockoutTieId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index("homeTeamId"),
        Index("awayTeamId"),
        Index("knockoutTieId"),
        Index("stage"),
        Index("status")
    ]
)
data class MatchEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    /** REGULAR | KNOCKOUT */
    val stage: String,
    /** Jornada number (1..N) for REGULAR, or round slots (8=R16, 4=QF, 2=SF, 1=F) for KNOCKOUT */
    val matchdayOrRound: Int,
    val knockoutTieId: Long? = null,
    /** 1 = Ida / único partido, 2 = Vuelta */
    val legNumber: Int = 1,
    val homeTeamId: Long,
    val awayTeamId: Long? = null,
    /** true when this record represents a bye (rest) in an odd-team matchday */
    val isBye: Boolean = false,
    /** PENDING | COMPLETED */
    val status: String = "PENDING",
    val homeGoals: Int = 0,
    val awayGoals: Int = 0,
    val homeFouls: Int = 0,
    val awayFouls: Int = 0,
    val mvpPlayerName: String? = null,
    val mvpRating: Double? = null
)
