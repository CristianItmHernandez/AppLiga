package com.hograna.liga.di;

import com.hograna.liga.data.local.AppDatabase;
import com.hograna.liga.data.local.dao.VideoHistoryDao;
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
public final class AppModule_ProvideVideoHistoryDaoFactory implements Factory<VideoHistoryDao> {
  private final Provider<AppDatabase> dbProvider;

  public AppModule_ProvideVideoHistoryDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public VideoHistoryDao get() {
    return provideVideoHistoryDao(dbProvider.get());
  }

  public static AppModule_ProvideVideoHistoryDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new AppModule_ProvideVideoHistoryDaoFactory(dbProvider);
  }

  public static VideoHistoryDao provideVideoHistoryDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideVideoHistoryDao(db));
  }
}
