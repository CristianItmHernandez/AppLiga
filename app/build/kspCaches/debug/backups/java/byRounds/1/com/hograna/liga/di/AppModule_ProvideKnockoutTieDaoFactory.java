package com.hograna.liga.di;

import com.hograna.liga.data.local.AppDatabase;
import com.hograna.liga.data.local.dao.KnockoutTieDao;
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
public final class AppModule_ProvideKnockoutTieDaoFactory implements Factory<KnockoutTieDao> {
  private final Provider<AppDatabase> dbProvider;

  public AppModule_ProvideKnockoutTieDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public KnockoutTieDao get() {
    return provideKnockoutTieDao(dbProvider.get());
  }

  public static AppModule_ProvideKnockoutTieDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new AppModule_ProvideKnockoutTieDaoFactory(dbProvider);
  }

  public static KnockoutTieDao provideKnockoutTieDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideKnockoutTieDao(db));
  }
}
