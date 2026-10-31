package com.hograna.liga.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hograna.liga.data.local.dao.KnockoutTieDao
import com.hograna.liga.data.local.dao.MatchDao
import com.hograna.liga.data.local.dao.StandingsDao
import com.hograna.liga.data.local.dao.StatsDao
import com.hograna.liga.data.local.dao.TeamDao
import com.hograna.liga.data.local.dao.VideoHistoryDao
import com.hograna.liga.data.local.entity.KnockoutTieEntity
import com.hograna.liga.data.local.entity.MatchEntity
import com.hograna.liga.data.local.entity.MatchPlayerEventEntity
import com.hograna.liga.data.local.entity.TeamEntity
import com.hograna.liga.data.local.entity.VideoHighlightEntity

@Database(
    entities = [
        TeamEntity::class,
        MatchEntity::class,
        MatchPlayerEventEntity::class,
        KnockoutTieEntity::class,
        VideoHighlightEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun teamDao(): TeamDao
    abstract fun matchDao(): MatchDao
    abstract fun standingsDao(): StandingsDao
    abstract fun statsDao(): StatsDao
    abstract fun knockoutTieDao(): KnockoutTieDao
    abstract fun videoHistoryDao(): VideoHistoryDao
}
