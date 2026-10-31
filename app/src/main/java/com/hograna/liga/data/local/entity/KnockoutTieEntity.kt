package com.hograna.liga.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "knockout_ties",
    foreignKeys = [
        ForeignKey(
            entity = TeamEntity::class,
            parentColumns = ["id"],
            childColumns = ["teamAId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = TeamEntity::class,
            parentColumns = ["id"],
            childColumns = ["teamBId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = TeamEntity::class,
            parentColumns = ["id"],
            childColumns = ["winnerTeamId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index("teamAId"),
        Index("teamBId"),
        Index("winnerTeamId"),
        Index(value = ["round", "slotIndex"], unique = true)
    ]
)
data class KnockoutTieEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    /** 8=R16, 4=QF, 2=SF, 1=F */
    val round: Int,
    /** Position in bracket: 0..7 for R16, 0..3 for QF, 0..1 for SF, 0 for Final */
    val slotIndex: Int,
    val teamAId: Long? = null,
    val teamBId: Long? = null,
    val teamAPenalties: Int? = null,
    val teamBPenalties: Int? = null,
    val winnerTeamId: Long? = null
)
