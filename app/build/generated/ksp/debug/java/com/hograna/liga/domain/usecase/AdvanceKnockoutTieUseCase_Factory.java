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
public final class AdvanceKnockoutTieUseCase_Factory implements Factory<AdvanceKnockoutTieUseCase> {
  private final Provider<TournamentRepository> repositoryProvider;

  public AdvanceKnockoutTieUseCase_Factory(Provider<TournamentRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public AdvanceKnockoutTieUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static AdvanceKnockoutTieUseCase_Factory create(
      Provider<TournamentRepository> repositoryProvider) {
    return new AdvanceKnockoutTieUseCase_Factory(repositoryProvider);
  }

  public static AdvanceKnockoutTieUseCase newInstance(TournamentRepository repository) {
    return new AdvanceKnockoutTieUseCase(repository);
  }
}
