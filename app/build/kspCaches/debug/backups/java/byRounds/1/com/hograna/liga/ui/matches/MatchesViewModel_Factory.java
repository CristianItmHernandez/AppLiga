package com.hograna.liga.ui.matches;

import com.hograna.liga.data.local.dao.StandingsDao;
import com.hograna.liga.data.repository.TournamentRepository;
import com.hograna.liga.domain.usecase.AdvanceKnockoutTieUseCase;
import com.hograna.liga.domain.usecase.CompleteMatchUseCase;
import com.hograna.liga.domain.usecase.GenerateMatchdayUseCase;
import com.hograna.liga.domain.usecase.InitializeKnockoutBracketUseCase;
import com.hograna.liga.domain.usecase.ShareMatchGraphicUseCase;
import com.hograna.liga.domain.usecase.UpsertManualMatchUseCase;
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
public final class MatchesViewModel_Factory implements Factory<MatchesViewModel> {
  private final Provider<TournamentRepository> repositoryProvider;

  private final Provider<GenerateMatchdayUseCase> generateMatchdayUseCaseProvider;

  private final Provider<CompleteMatchUseCase> completeMatchUseCaseProvider;

  private final Provider<AdvanceKnockoutTieUseCase> advanceKnockoutTieUseCaseProvider;

  private final Provider<ShareMatchGraphicUseCase> shareMatchGraphicUseCaseProvider;

  private final Provider<UpsertManualMatchUseCase> upsertManualMatchUseCaseProvider;

  private final Provider<InitializeKnockoutBracketUseCase> initializeKnockoutBracketUseCaseProvider;

  private final Provider<StandingsDao> standingsDaoProvider;

  public MatchesViewModel_Factory(Provider<TournamentRepository> repositoryProvider,
      Provider<GenerateMatchdayUseCase> generateMatchdayUseCaseProvider,
      Provider<CompleteMatchUseCase> completeMatchUseCaseProvider,
      Provider<AdvanceKnockoutTieUseCase> advanceKnockoutTieUseCaseProvider,
      Provider<ShareMatchGraphicUseCase> shareMatchGraphicUseCaseProvider,
      Provider<UpsertManualMatchUseCase> upsertManualMatchUseCaseProvider,
      Provider<InitializeKnockoutBracketUseCase> initializeKnockoutBracketUseCaseProvider,
      Provider<StandingsDao> standingsDaoProvider) {
    this.repositoryProvider = repositoryProvider;
    this.generateMatchdayUseCaseProvider = generateMatchdayUseCaseProvider;
    this.completeMatchUseCaseProvider = completeMatchUseCaseProvider;
    this.advanceKnockoutTieUseCaseProvider = advanceKnockoutTieUseCaseProvider;
    this.shareMatchGraphicUseCaseProvider = shareMatchGraphicUseCaseProvider;
    this.upsertManualMatchUseCaseProvider = upsertManualMatchUseCaseProvider;
    this.initializeKnockoutBracketUseCaseProvider = initializeKnockoutBracketUseCaseProvider;
    this.standingsDaoProvider = standingsDaoProvider;
  }

  @Override
  public MatchesViewModel get() {
    return newInstance(repositoryProvider.get(), generateMatchdayUseCaseProvider.get(), completeMatchUseCaseProvider.get(), advanceKnockoutTieUseCaseProvider.get(), shareMatchGraphicUseCaseProvider.get(), upsertManualMatchUseCaseProvider.get(), initializeKnockoutBracketUseCaseProvider.get(), standingsDaoProvider.get());
  }

  public static MatchesViewModel_Factory create(Provider<TournamentRepository> repositoryProvider,
      Provider<GenerateMatchdayUseCase> generateMatchdayUseCaseProvider,
      Provider<CompleteMatchUseCase> completeMatchUseCaseProvider,
      Provider<AdvanceKnockoutTieUseCase> advanceKnockoutTieUseCaseProvider,
      Provider<ShareMatchGraphicUseCase> shareMatchGraphicUseCaseProvider,
      Provider<UpsertManualMatchUseCase> upsertManualMatchUseCaseProvider,
      Provider<InitializeKnockoutBracketUseCase> initializeKnockoutBracketUseCaseProvider,
      Provider<StandingsDao> standingsDaoProvider) {
    return new MatchesViewModel_Factory(repositoryProvider, generateMatchdayUseCaseProvider, completeMatchUseCaseProvider, advanceKnockoutTieUseCaseProvider, shareMatchGraphicUseCaseProvider, upsertManualMatchUseCaseProvider, initializeKnockoutBracketUseCaseProvider, standingsDaoProvider);
  }

  public static MatchesViewModel newInstance(TournamentRepository repository,
      GenerateMatchdayUseCase generateMatchdayUseCase, CompleteMatchUseCase completeMatchUseCase,
      AdvanceKnockoutTieUseCase advanceKnockoutTieUseCase,
      ShareMatchGraphicUseCase shareMatchGraphicUseCase,
      UpsertManualMatchUseCase upsertManualMatchUseCase,
      InitializeKnockoutBracketUseCase initializeKnockoutBracketUseCase,
      StandingsDao standingsDao) {
    return new MatchesViewModel(repository, generateMatchdayUseCase, completeMatchUseCase, advanceKnockoutTieUseCase, shareMatchGraphicUseCase, upsertManualMatchUseCase, initializeKnockoutBracketUseCase, standingsDao);
  }
}
