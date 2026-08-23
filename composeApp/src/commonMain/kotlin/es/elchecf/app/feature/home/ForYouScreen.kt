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
import androidx.compose.ui.Modifier
import es.elchecf.app.designsystem.component.SectionHeader
import es.elchecf.app.designsystem.component.VersusCard
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme
import es.elchecf.app.domain.model.MatchStatus
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ForYouRoute(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<ForYouViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    ForYouScreen(
        uiState = uiState,
        onSubmitPrediction = viewModel::submitPrediction,
        onFichaDelPartidoClick = {}, // FASE 5: navegar a la ficha del partido cuando exista esa pantalla
        onPlayQuizClick = {}, // FASE del quiz sin numerar todavía en CLAUDE.md §8
        onValorarClick = {}, // FASE del quiz sin numerar todavía en CLAUDE.md §8
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
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(ElcheSpacing.screenMargin),
    ) {
        SectionHeader(title = "Para ti")

        val match = uiState.match
        when {
            uiState.isLoading -> Text(text = "Cargando…", style = ElcheTheme.typography.bodyS)
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
            }
        }
    }
}
