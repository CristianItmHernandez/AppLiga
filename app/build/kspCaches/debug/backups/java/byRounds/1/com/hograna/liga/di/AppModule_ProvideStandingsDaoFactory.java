package com.hograna.liga.di;

import com.hograna.liga.data.local.AppDatabase;
import com.hograna.liga.data.local.dao.StandingsDao;
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
public final class AppModule_ProvideStandingsDaoFactory implements Factory<StandingsDao> {
  private final Provider<AppDatabase> dbProvider;

  public AppModule_ProvideStandingsDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public StandingsDao get() {
    return provideStandingsDao(dbProvider.get());
  }

  public static AppModule_ProvideStandingsDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new AppModule_ProvideStandingsDaoFactory(dbProvider);
  }

  public static StandingsDao provideStandingsDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideStandingsDao(db));
  }
}
