package com.hograna.liga.di;

import com.hograna.liga.data.local.AppDatabase;
import com.hograna.liga.data.local.dao.KnockoutTieDao;
import com.hograna.liga.data.local.dao.MatchDao;
import com.hograna.liga.data.local.dao.TeamDao;
import com.hograna.liga.data.local.dao.VideoHistoryDao;
import com.hograna.liga.data.repository.TournamentRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideTournamentRepositoryFactory implements Factory<TournamentRepository> {
  private final Provider<TeamDao> teamDaoProvider;

  private final Provider<MatchDao> matchDaoProvider;

  private final Provider<KnockoutTieDao> knockoutTieDaoProvider;

  private final Provider<VideoHistoryDao> videoHistoryDaoProvider;

  private final Provider<AppDatabase> databaseProvider;

  public AppModule_ProvideTournamentRepositoryFactory(Provider<TeamDao> teamDaoProvider,
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
    return provideTournamentRepository(teamDaoProvider.get(), matchDaoProvider.get(), knockoutTieDaoProvider.get(), videoHistoryDaoProvider.get(), databaseProvider.get());
  }

  public static AppModule_ProvideTournamentRepositoryFactory create(
      Provider<TeamDao> teamDaoProvider, Provider<MatchDao> matchDaoProvider,
      Provider<KnockoutTieDao> knockoutTieDaoProvider,
      Provider<VideoHistoryDao> videoHistoryDaoProvider, Provider<AppDatabase> databaseProvider) {
    return new AppModule_ProvideTournamentRepositoryFactory(teamDaoProvider, matchDaoProvider, knockoutTieDaoProvider, videoHistoryDaoProvider, databaseProvider);
  }

  public static TournamentRepository provideTournamentRepository(TeamDao teamDao, MatchDao matchDao,
      KnockoutTieDao knockoutTieDao, VideoHistoryDao videoHistoryDao, AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideTournamentRepository(teamDao, matchDao, knockoutTieDao, videoHistoryDao, database));
  }
}
