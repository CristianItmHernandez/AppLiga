package com.hograna.liga.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hograna.liga.data.local.entity.VideoHighlightEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: VideoHighlightEntity): Long

    @Delete
    suspend fun deleteVideo(video: VideoHighlightEntity)

    @Query("SELECT * FROM video_highlights ORDER BY createdAt DESC")
    fun getAllVideos(): Flow<List<VideoHighlightEntity>>

    @Query("SELECT * FROM video_highlights WHERE id = :id")
    suspend fun getVideoById(id: Long): VideoHighlightEntity?
}
