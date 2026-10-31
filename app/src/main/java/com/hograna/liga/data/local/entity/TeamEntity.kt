package com.hograna.liga.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "teams",
    indices = [Index(value = ["name"], unique = true)]
)
data class TeamEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    /** UCL | UEL | LIBERTADORES | CUSTOM */
    val leagueOrigin: String,
    /** Drawable resource name or persistent URI from picker */
    val crestUri: String,
    val captainName: String,
    val byeCount: Int = 0
)
