package com.hograna.liga.ui.standings;

import com.hograna.liga.data.local.dao.MatchDao;
import com.hograna.liga.data.local.dao.StandingsDao;
import com.hograna.liga.data.local.dao.TeamDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class StandingsViewModel_Factory implements Factory<StandingsViewModel> {
  private final Provider<StandingsDao> standingsDaoProvider;

  private final Provider<MatchDao> matchDaoProvider;

  private final Provider<TeamDao> teamDaoProvider;

  public StandingsViewModel_Factory(Provider<StandingsDao> standingsDaoProvider,
      Provider<MatchDao> matchDaoProvider, Provider<TeamDao> teamDaoProvider) {
    this.standingsDaoProvider = standingsDaoProvider;
    this.matchDaoProvider = matchDaoProvider;
    this.teamDaoProvider = teamDaoProvider;
  }

  @Override
  public StandingsViewModel get() {
    return newInstance(standingsDaoProvider.get(), matchDaoProvider.get(), teamDaoProvider.get());
  }

  public static StandingsViewModel_Factory create(Provider<StandingsDao> standingsDaoProvider,
      Provider<MatchDao> matchDaoProvider, Provider<TeamDao> teamDaoProvider) {
    return new StandingsViewModel_Factory(standingsDaoProvider, matchDaoProvider, teamDaoProvider);
  }

  public static StandingsViewModel newInstance(StandingsDao standingsDao, MatchDao matchDao,
      TeamDao teamDao) {
    return new StandingsViewModel(standingsDao, matchDao, teamDao);
  }
}
