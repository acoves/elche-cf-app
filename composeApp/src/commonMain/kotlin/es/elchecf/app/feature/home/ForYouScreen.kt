package es.elchecf.app.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import es.elchecf.app.designsystem.component.SectionHeader
import es.elchecf.app.designsystem.component.VersusCard
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.domain.model.Match
import es.elchecf.app.domain.model.MatchStatus
import es.elchecf.app.domain.model.Team
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days

// FASE 4 (en construcción): VersusCard + predictor ya reales. Quiz, valorar partido, y el
// ForYouViewModel que sustituye este partido de ejemplo, llegan en el siguiente chunk.
private val sampleMatch =
    Match(
        id = "demo-match-1",
        home = Team("elche-cf", "Elche CF", "ELCHE", "", "#05642C"),
        away = Team("rival-demo", "Real Madrid", "R. MADRID", "", "#1B458F"),
        kickoffInstant = Clock.System.now() + 3.days,
        competition = "LaLiga",
        venue = "Martínez Valero",
        status = MatchStatus.Scheduled,
    )

@Composable
fun ForYouScreen(modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(ElcheSpacing.screenMargin),
    ) {
        SectionHeader(title = "Para ti")
        VersusCard(
            match = sampleMatch,
            onFichaDelPartidoClick = {},
            modifier = Modifier.fillMaxWidth().padding(top = ElcheSpacing.lg),
        )
        PredictorCard(
            home = sampleMatch.home,
            away = sampleMatch.away,
            locked = sampleMatch.status != MatchStatus.Scheduled,
            onSubmit = { _, _ -> },
            modifier = Modifier.fillMaxWidth().padding(top = ElcheSpacing.lg),
        )
    }
}
