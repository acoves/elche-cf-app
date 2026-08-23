package es.elchecf.app.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.elchecf.app.core.util.CountdownParts
import es.elchecf.app.core.util.countdownFlow
import es.elchecf.app.core.util.toColorOrNull
import es.elchecf.app.core.util.toKickoffLabel
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheShape
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme
import es.elchecf.app.domain.model.Match
import es.elchecf.app.domain.model.MatchStatus
import es.elchecf.app.domain.model.Team
import kotlinx.coroutines.flow.collectLatest
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days

/**
 * Componente insignia (CLAUDE.md §4.5). Fondo dividido por los colores de marca de cada equipo,
 * cuenta atrás en dorado, pie blanco con fecha/hora/competición.
 * FASE 8: si `match.status == Live`, los bloques de cuenta atrás se sustituyen por el marcador.
 */
@Composable
fun VersusCard(
    match: Match,
    onFichaDelPartidoClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var countdown by remember { mutableStateOf(CountdownParts(0, 0, 0, 0, isPast = false)) }
    LaunchedEffect(match.kickoffInstant) {
        countdownFlow(match.kickoffInstant).collectLatest { countdown = it }
    }

    val homeColor = match.home.primaryColorHex.toColorOrNull() ?: ElcheColor.Green
    val awayColor = match.away.primaryColorHex.toColorOrNull() ?: ElcheColor.CrestBlue

    Column(modifier = modifier.clip(ElcheShape.CardLarge)) {
        Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
            Row(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.weight(1f).fillMaxSize().background(homeColor))
                Box(modifier = Modifier.weight(1f).fillMaxSize().background(awayColor))
            }
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(ElcheSpacing.md),
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(ElcheSpacing.sm)) {
                    CountdownBlock(value = countdown.days, label = "DÍAS")
                    CountdownBlock(value = countdown.hours, label = "H")
                    CountdownBlock(value = countdown.minutes, label = "MIN")
                    CountdownBlock(value = countdown.seconds, label = "S")
                }
                ElcheButton(
                    text = "Ficha del partido",
                    onClick = onFichaDelPartidoClick,
                    variant = ElcheButtonVariant.Accent,
                )
            }
        }
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(ElcheColor.White)
                    .padding(horizontal = ElcheSpacing.lg, vertical = ElcheSpacing.md),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(text = match.kickoffInstant.toKickoffLabel(), style = ElcheTheme.typography.bodyS)
            Text(
                text = "${match.competition} · ${match.venue}",
                style = ElcheTheme.typography.bodyS,
                color = ElcheColor.InkMuted,
            )
        }
    }
}

@Composable
private fun CountdownBlock(
    value: Long,
    label: String,
) {
    Column(
        modifier =
            Modifier
                .width(56.dp)
                .background(color = Color.White.copy(alpha = 0.15f), shape = ElcheShape.Card)
                .padding(vertical = ElcheSpacing.sm),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = value.toString().padStart(2, '0'),
            style = ElcheTheme.typography.monoNum,
            color = ElcheColor.Gold,
            textAlign = TextAlign.Center,
        )
        Text(
            text = label,
            style = ElcheTheme.typography.label,
            color = ElcheColor.White,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
private fun VersusCardPreview() {
    val sampleMatch =
        Match(
            id = "preview",
            home =
                Team(
                    id = "elche-cf",
                    name = "Elche CF",
                    shortName = "ELCHE",
                    crestUrl = "",
                    primaryColorHex = "#05642C",
                ),
            away =
                Team(
                    id = "rival",
                    name = "Real Madrid",
                    shortName = "R. MADRID",
                    crestUrl = "",
                    primaryColorHex = "#1B458F",
                ),
            kickoffInstant = Clock.System.now() + 3.days,
            competition = "LaLiga",
            venue = "Martínez Valero",
            status = MatchStatus.Scheduled,
        )
    ElcheTheme {
        VersusCard(match = sampleMatch, onFichaDelPartidoClick = {}, modifier = Modifier.padding(ElcheSpacing.lg))
    }
}
