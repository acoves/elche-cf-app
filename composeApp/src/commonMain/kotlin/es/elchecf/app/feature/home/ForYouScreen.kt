package es.elchecf.app.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import es.elchecf.app.designsystem.component.SectionHeader
import es.elchecf.app.designsystem.component.VersusCard
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme
import es.elchecf.app.domain.model.ClubTeam
import es.elchecf.app.domain.model.MatchStatus
import es.elchecf.app.feature.gamezone.GameZoneScreen
import es.elchecf.app.feature.home.favoriteplayers.FavoriteCaptainSelectScreen
import es.elchecf.app.feature.home.favoriteplayers.FavoritePlayersCard
import es.elchecf.app.feature.home.favoriteplayers.FavoritePlayersSelectScreen
import es.elchecf.app.feature.home.favoriteplayers.elcheSquad2627
import es.elchecf.app.feature.home.favoriteteam.FavoriteTeamsSection
import es.elchecf.app.feature.home.news.NewsSection
import org.koin.compose.viewmodel.koinViewModel

private sealed interface ForYouSubScreen {
    data object Main : ForYouSubScreen

    data object GameZone : ForYouSubScreen

    data object FavoritePlayersSelect : ForYouSubScreen

    data object FavoriteCaptainSelect : ForYouSubScreen
}

@Composable
fun ForYouRoute(
    onNavigateToShop: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val viewModel = koinViewModel<ForYouViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    ForYouScreen(
        uiState = uiState,
        onSubmitPrediction = viewModel::submitPrediction,
        onFichaDelPartidoClick = {}, // FASE 5: navegar a la ficha del partido cuando exista esa pantalla
        onPlayQuizClick = {}, // FASE del quiz sin numerar todavía en CLAUDE.md §8
        onValorarClick = {}, // FASE del quiz sin numerar todavía en CLAUDE.md §8
        onStoreClick = onNavigateToShop,
        modifier = modifier,
    )
}

@Composable
fun ForYouScreen(
    uiState: ForYouUiState,
    onSubmitPrediction: (homeGoals: Int, awayGoals: Int) -> Unit,
    onFichaDelPartidoClick: () -> Unit,
    onPlayQuizClick: () -> Unit,
    onValorarClick: () -> Unit,
    onStoreClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var subScreen by remember { mutableStateOf<ForYouSubScreen>(ForYouSubScreen.Main) }
    var favoritePlayerNumbers by remember { mutableStateOf<Set<Int>>(emptySet()) }
    var captainNumber by remember { mutableStateOf<Int?>(null) }
    var favoriteTeams by remember { mutableStateOf<Set<ClubTeam>>(emptySet()) }

    when (subScreen) {
        ForYouSubScreen.Main -> Unit
        ForYouSubScreen.GameZone -> {
            GameZoneScreen(match = uiState.match, onBack = { subScreen = ForYouSubScreen.Main })
            return
        }
        ForYouSubScreen.FavoritePlayersSelect -> {
            FavoritePlayersSelectScreen(
                initialSelection = favoritePlayerNumbers,
                onBack = { subScreen = ForYouSubScreen.Main },
                onContinue = { selection ->
                    favoritePlayerNumbers = selection
                    if (captainNumber !in selection) captainNumber = null
                    subScreen = ForYouSubScreen.FavoriteCaptainSelect
                },
            )
            return
        }
        ForYouSubScreen.FavoriteCaptainSelect -> {
            FavoriteCaptainSelectScreen(
                favorites = elcheSquad2627.filter { it.number in favoritePlayerNumbers },
                initialCaptain = captainNumber,
                onBack = { subScreen = ForYouSubScreen.FavoritePlayersSelect },
                onConfirm = { number ->
                    captainNumber = number
                    subScreen = ForYouSubScreen.Main
                },
            )
            return
        }
    }

    Column(modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Column(modifier = Modifier.padding(horizontal = ElcheSpacing.screenMargin)) {
            SectionHeader(title = "Para ti", modifier = Modifier.padding(top = ElcheSpacing.screenMargin))

            val match = uiState.match
            when {
                uiState.isLoading -> Text(text = "Cargando…", style = ElcheTheme.typography.bodyS)
                // Un error de carga (sin partido) bloquea la pantalla; un error de acción (p.ej.
                // al enviar la predicción, con partido ya cargado) solo se muestra como aviso más
                // abajo.
                match == null && uiState.error != null ->
                    Text(text = uiState.error, style = ElcheTheme.typography.bodyS)
                match == null -> Text(text = "No hay próximo partido programado.", style = ElcheTheme.typography.bodyS)
                else -> {
                    VersusCard(
                        match = match,
                        onFichaDelPartidoClick = onFichaDelPartidoClick,
                        modifier = Modifier.fillMaxWidth().padding(top = ElcheSpacing.lg),
                    )
                    PredictorCard(
                        home = match.home,
                        away = match.away,
                        locked = uiState.predictionSent || match.status != MatchStatus.Scheduled,
                        onSubmit = onSubmitPrediction,
                        modifier = Modifier.fillMaxWidth().padding(top = ElcheSpacing.lg),
                    )
                    QuizCard(
                        score = uiState.quizScore,
                        onPlayClick = onPlayQuizClick,
                        modifier = Modifier.fillMaxWidth().padding(top = ElcheSpacing.lg),
                    )
                    if (uiState.showValorateMatch) {
                        ValorateMatchCard(
                            onValorarClick = onValorarClick,
                            modifier = Modifier.fillMaxWidth().padding(top = ElcheSpacing.lg),
                        )
                    }
                    if (uiState.error != null) {
                        Text(
                            text = uiState.error,
                            style = ElcheTheme.typography.bodyS,
                            modifier = Modifier.padding(top = ElcheSpacing.md),
                        )
                    }
                }
            }
        }

        PressConferenceBanner(
            modifier =
                Modifier.fillMaxWidth().padding(
                    start = ElcheSpacing.screenMargin,
                    end = ElcheSpacing.screenMargin,
                    top = ElcheSpacing.xxl,
                ),
        )

        NewsSection(
            modifier =
                Modifier.fillMaxWidth().padding(
                    start = ElcheSpacing.screenMargin,
                    end = ElcheSpacing.screenMargin,
                    top = ElcheSpacing.xxl,
                ),
        )

        GameZoneBanner(
            onGameZoneClick = { subScreen = ForYouSubScreen.GameZone },
            modifier = Modifier.padding(horizontal = ElcheSpacing.screenMargin, vertical = ElcheSpacing.xxl),
        )

        StoreCarouselSection(
            onStoreClick = onStoreClick,
            modifier = Modifier.padding(bottom = ElcheSpacing.xxl),
        )

        FavoritePlayersCard(
            favorites = elcheSquad2627.filter { it.number in favoritePlayerNumbers },
            captain = elcheSquad2627.find { it.number == captainNumber },
            onEditClick = { subScreen = ForYouSubScreen.FavoritePlayersSelect },
            onBuyShirtClick = onStoreClick,
            modifier =
                Modifier.fillMaxWidth().padding(
                    start = ElcheSpacing.screenMargin,
                    end = ElcheSpacing.screenMargin,
                    top = ElcheSpacing.xl,
                ),
        )

        FavoriteTeamsSection(
            selectedTeams = favoriteTeams,
            onToggle = { team ->
                favoriteTeams = if (team in favoriteTeams) favoriteTeams - team else favoriteTeams + team
            },
            modifier =
                Modifier.fillMaxWidth().padding(
                    start = ElcheSpacing.screenMargin,
                    end = ElcheSpacing.screenMargin,
                    top = ElcheSpacing.xxl,
                    bottom = ElcheSpacing.xxl,
                ),
        )
    }
}
