package com.hograna.liga.di;

import com.hograna.liga.data.local.AppDatabase;
import com.hograna.liga.data.local.dao.StatsDao;
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
public final class AppModule_ProvideStatsDaoFactory implements Factory<StatsDao> {
  private final Provider<AppDatabase> dbProvider;

  public AppModule_ProvideStatsDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public StatsDao get() {
    return provideStatsDao(dbProvider.get());
  }

  public static AppModule_ProvideStatsDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new AppModule_ProvideStatsDaoFactory(dbProvider);
  }

  public static StatsDao provideStatsDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideStatsDao(db));
  }
}
