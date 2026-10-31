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
public final class GenerateMatchdayUseCase_Factory implements Factory<GenerateMatchdayUseCase> {
  private final Provider<TournamentRepository> repositoryProvider;

  public GenerateMatchdayUseCase_Factory(Provider<TournamentRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GenerateMatchdayUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GenerateMatchdayUseCase_Factory create(
      Provider<TournamentRepository> repositoryProvider) {
    return new GenerateMatchdayUseCase_Factory(repositoryProvider);
  }

  public static GenerateMatchdayUseCase newInstance(TournamentRepository repository) {
    return new GenerateMatchdayUseCase(repository);
  }
}
