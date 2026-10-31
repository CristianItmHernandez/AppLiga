package com.hograna.liga.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "video_highlights")
data class VideoHighlightEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val homeTeamName: String,
    val awayTeamName: String,
    /** e.g. "3 - 1" */
    val scoreSummary: String,
    /** Comma-separated scorer names */
    val scorersList: String,
    /** GOOGLE_DRIVE | LOCAL_URI */
    val videoSourceType: String,
    /** Transformed Drive stream URL or persistent local URI */
    val videoUrlOrUri: String,
    val createdAt: Long = System.currentTimeMillis()
)
