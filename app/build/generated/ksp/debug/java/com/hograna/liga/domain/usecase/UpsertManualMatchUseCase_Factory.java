package com.hograna.liga.domain.usecase;

import com.hograna.liga.data.repository.TournamentRepository;
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
public final class UpsertManualMatchUseCase_Factory implements Factory<UpsertManualMatchUseCase> {
  private final Provider<TournamentRepository> repositoryProvider;

  public UpsertManualMatchUseCase_Factory(Provider<TournamentRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public UpsertManualMatchUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static UpsertManualMatchUseCase_Factory create(
      Provider<TournamentRepository> repositoryProvider) {
    return new UpsertManualMatchUseCase_Factory(repositoryProvider);
  }

  public static UpsertManualMatchUseCase newInstance(TournamentRepository repository) {
    return new UpsertManualMatchUseCase(repository);
  }
}
