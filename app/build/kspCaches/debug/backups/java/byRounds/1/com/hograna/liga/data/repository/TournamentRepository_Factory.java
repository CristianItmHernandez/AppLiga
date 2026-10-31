package com.hograna.liga.data.repository;

import com.hograna.liga.data.local.AppDatabase;
import com.hograna.liga.data.local.dao.KnockoutTieDao;
import com.hograna.liga.data.local.dao.MatchDao;
import com.hograna.liga.data.local.dao.TeamDao;
import com.hograna.liga.data.local.dao.VideoHistoryDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class TournamentRepository_Factory implements Factory<TournamentRepository> {
  private final Provider<TeamDao> teamDaoProvider;

  private final Provider<MatchDao> matchDaoProvider;

  private final Provider<KnockoutTieDao> knockoutTieDaoProvider;

  private final Provider<VideoHistoryDao> videoHistoryDaoProvider;

  private final Provider<AppDatabase> databaseProvider;

  public TournamentRepository_Factory(Provider<TeamDao> teamDaoProvider,
      Provider<MatchDao> matchDaoProvider, Provider<KnockoutTieDao> knockoutTieDaoProvider,
      Provider<VideoHistoryDao> videoHistoryDaoProvider, Provider<AppDatabase> databaseProvider) {
    this.teamDaoProvider = teamDaoProvider;
    this.matchDaoProvider = matchDaoProvider;
    this.knockoutTieDaoProvider = knockoutTieDaoProvider;
    this.videoHistoryDaoProvider = videoHistoryDaoProvider;
    this.databaseProvider = databaseProvider;
  }

  @Override
  public TournamentRepository get() {
    return newInstance(teamDaoProvider.get(), matchDaoProvider.get(), knockoutTieDaoProvider.get(), videoHistoryDaoProvider.get(), databaseProvider.get());
  }

  public static TournamentRepository_Factory create(Provider<TeamDao> teamDaoProvider,
      Provider<MatchDao> matchDaoProvider, Provider<KnockoutTieDao> knockoutTieDaoProvider,
      Provider<VideoHistoryDao> videoHistoryDaoProvider, Provider<AppDatabase> databaseProvider) {
    return new TournamentRepository_Factory(teamDaoProvider, matchDaoProvider, knockoutTieDaoProvider, videoHistoryDaoProvider, databaseProvider);
  }

  public static TournamentRepository newInstance(TeamDao teamDao, MatchDao matchDao,
      KnockoutTieDao knockoutTieDao, VideoHistoryDao videoHistoryDao, AppDatabase database) {
    return new TournamentRepository(teamDao, matchDao, knockoutTieDao, videoHistoryDao, database);
  }
}
