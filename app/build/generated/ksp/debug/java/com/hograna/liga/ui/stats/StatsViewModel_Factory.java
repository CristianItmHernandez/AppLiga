package com.hograna.liga.ui.stats;

import com.hograna.liga.data.local.dao.StatsDao;
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
public final class StatsViewModel_Factory implements Factory<StatsViewModel> {
  private final Provider<StatsDao> statsDaoProvider;

  public StatsViewModel_Factory(Provider<StatsDao> statsDaoProvider) {
    this.statsDaoProvider = statsDaoProvider;
  }

  @Override
  public StatsViewModel get() {
    return newInstance(statsDaoProvider.get());
  }

  public static StatsViewModel_Factory create(Provider<StatsDao> statsDaoProvider) {
    return new StatsViewModel_Factory(statsDaoProvider);
  }

  public static StatsViewModel newInstance(StatsDao statsDao) {
    return new StatsViewModel(statsDao);
  }
}
