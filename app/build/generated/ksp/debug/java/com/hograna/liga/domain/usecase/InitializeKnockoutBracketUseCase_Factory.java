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
public final class InitializeKnockoutBracketUseCase_Factory implements Factory<InitializeKnockoutBracketUseCase> {
  private final Provider<TournamentRepository> repositoryProvider;

  public InitializeKnockoutBracketUseCase_Factory(
      Provider<TournamentRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public InitializeKnockoutBracketUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static InitializeKnockoutBracketUseCase_Factory create(
      Provider<TournamentRepository> repositoryProvider) {
    return new InitializeKnockoutBracketUseCase_Factory(repositoryProvider);
  }

  public static InitializeKnockoutBracketUseCase newInstance(TournamentRepository repository) {
    return new InitializeKnockoutBracketUseCase(repository);
  }
}
