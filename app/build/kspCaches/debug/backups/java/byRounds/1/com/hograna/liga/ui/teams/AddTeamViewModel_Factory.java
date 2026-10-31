package com.hograna.liga.ui.teams;

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
public final class AddTeamViewModel_Factory implements Factory<AddTeamViewModel> {
  private final Provider<TournamentRepository> repositoryProvider;

  public AddTeamViewModel_Factory(Provider<TournamentRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public AddTeamViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static AddTeamViewModel_Factory create(Provider<TournamentRepository> repositoryProvider) {
    return new AddTeamViewModel_Factory(repositoryProvider);
  }

  public static AddTeamViewModel newInstance(TournamentRepository repository) {
    return new AddTeamViewModel(repository);
  }
}
