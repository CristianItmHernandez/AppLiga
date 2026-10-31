package com.hograna.liga.domain.usecase;

import android.content.Context;
import com.hograna.liga.data.repository.TournamentRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class ShareMatchGraphicUseCase_Factory implements Factory<ShareMatchGraphicUseCase> {
  private final Provider<Context> contextProvider;

  private final Provider<TournamentRepository> repositoryProvider;

  public ShareMatchGraphicUseCase_Factory(Provider<Context> contextProvider,
      Provider<TournamentRepository> repositoryProvider) {
    this.contextProvider = contextProvider;
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ShareMatchGraphicUseCase get() {
    return newInstance(contextProvider.get(), repositoryProvider.get());
  }

  public static ShareMatchGraphicUseCase_Factory create(Provider<Context> contextProvider,
      Provider<TournamentRepository> repositoryProvider) {
    return new ShareMatchGraphicUseCase_Factory(contextProvider, repositoryProvider);
  }

  public static ShareMatchGraphicUseCase newInstance(Context context,
      TournamentRepository repository) {
    return new ShareMatchGraphicUseCase(context, repository);
  }
}
