package com.hograna.liga.ui.videos;

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
public final class VideoViewModel_Factory implements Factory<VideoViewModel> {
  private final Provider<TournamentRepository> repositoryProvider;

  public VideoViewModel_Factory(Provider<TournamentRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public VideoViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static VideoViewModel_Factory create(Provider<TournamentRepository> repositoryProvider) {
    return new VideoViewModel_Factory(repositoryProvider);
  }

  public static VideoViewModel newInstance(TournamentRepository repository) {
    return new VideoViewModel(repository);
  }
}
