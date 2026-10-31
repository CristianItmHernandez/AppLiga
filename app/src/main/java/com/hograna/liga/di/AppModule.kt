package com.hograna.liga.di

import android.content.Context
import androidx.room.Room
import com.hograna.liga.data.local.AppDatabase
import com.hograna.liga.data.local.dao.KnockoutTieDao
import com.hograna.liga.data.local.dao.MatchDao
import com.hograna.liga.data.local.dao.StandingsDao
import com.hograna.liga.data.local.dao.StatsDao
import com.hograna.liga.data.local.dao.TeamDao
import com.hograna.liga.data.local.dao.VideoHistoryDao
import com.hograna.liga.data.repository.TournamentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "liga_hog_rana.db")
            .build()

    @Provides
    @Singleton
    fun provideTeamDao(db: AppDatabase): TeamDao = db.teamDao()

    @Provides
    @Singleton
    fun provideMatchDao(db: AppDatabase): MatchDao = db.matchDao()

    @Provides
    @Singleton
    fun provideStandingsDao(db: AppDatabase): StandingsDao = db.standingsDao()

    @Provides
    @Singleton
    fun provideStatsDao(db: AppDatabase): StatsDao = db.statsDao()

    @Provides
    @Singleton
    fun provideKnockoutTieDao(db: AppDatabase): KnockoutTieDao = db.knockoutTieDao()

    @Provides
    @Singleton
    fun provideVideoHistoryDao(db: AppDatabase): VideoHistoryDao = db.videoHistoryDao()

    @Provides
    @Singleton
    fun provideTournamentRepository(
        teamDao: TeamDao,
        matchDao: MatchDao,
        knockoutTieDao: KnockoutTieDao,
        videoHistoryDao: VideoHistoryDao,
        database: AppDatabase
    ): TournamentRepository =
        TournamentRepository(teamDao, matchDao, knockoutTieDao, videoHistoryDao, database)
}
