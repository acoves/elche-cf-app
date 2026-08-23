package es.elchecf.app.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.elchecf.app.core.util.toColorOrNull
import es.elchecf.app.designsystem.component.ElcheButton
import es.elchecf.app.designsystem.component.ElcheCard
import es.elchecf.app.designsystem.icon.ElchePredictorIcon
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme
import es.elchecf.app.domain.model.Team

/**
 * "Acierta el resultado" (CLAUDE.md §5.1). [locked] = el partido ya ha empezado: se bloquea la
 * edición. Estado del marcador en memoria (Fase 8 lo persiste); [onSubmit] entrega la predicción
 * final a quien llame (el ViewModel, cuando exista).
 */
@Composable
fun PredictorCard(
    home: Team,
    away: Team,
    locked: Boolean,
    onSubmit: (homeGoals: Int, awayGoals: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var homeGoals by remember { mutableIntStateOf(0) }
    var awayGoals by remember { mutableIntStateOf(0) }
    var submitted by remember { mutableIntStateOf(0) } // >0 tras enviar; simple flag sin más estado

    ElcheCard(modifier = modifier) {
        Text(text = "Acierta el resultado", style = ElcheTheme.typography.titleM)
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = ElcheSpacing.md),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            TeamScoreColumn(
                team = home,
                score = homeGoals,
                enabled = !locked,
                onIncrement = { homeGoals++ },
                onDecrement = { if (homeGoals > 0) homeGoals-- },
            )
            Text(
                text = "–",
                style = ElcheTheme.typography.titleL,
                modifier = Modifier.align(Alignment.CenterVertically),
            )
            TeamScoreColumn(
                team = away,
                score = awayGoals,
                enabled = !locked,
                onIncrement = { awayGoals++ },
                onDecrement = { if (awayGoals > 0) awayGoals-- },
            )
        }
        ElcheButton(
            text = if (locked) "Predicciones cerradas" else "Enviar predicción",
            onClick = {
                onSubmit(homeGoals, awayGoals)
                submitted++
            },
            enabled = !locked,
            modifier = Modifier.fillMaxWidth().padding(top = ElcheSpacing.lg),
        )
        if (submitted > 0 && !locked) {
            Text(
                text = "Predicción enviada: $homeGoals - $awayGoals",
                style = ElcheTheme.typography.bodyS,
                color = ElcheColor.InkMuted,
                modifier = Modifier.padding(top = ElcheSpacing.sm),
            )
        }
    }
}

@Composable
private fun TeamScoreColumn(
    team: Team,
    score: Int,
    enabled: Boolean,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TeamBadge(team)
        Text(
            text = team.shortName,
            style = ElcheTheme.typography.label,
            modifier = Modifier.padding(top = ElcheSpacing.xs),
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            ScoreStepIconButton(
                icon = ElchePredictorIcon.Decrement,
                enabled = enabled && score > 0,
                onClick = onDecrement,
            )
            Text(
                text = score.toString(),
                style = ElcheTheme.typography.titleL,
                textAlign = TextAlign.Center,
                modifier = Modifier.size(32.dp),
            )
            ScoreStepIconButton(icon = ElchePredictorIcon.Increment, enabled = enabled, onClick = onIncrement)
        }
    }
}

@Composable
private fun ScoreStepIconButton(
    icon: ImageVector,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    IconButton(onClick = onClick, enabled = enabled) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (enabled) ElcheColor.Green else ElcheColor.Divider,
        )
    }
}

@Composable
private fun TeamBadge(team: Team) {
    val color = team.primaryColorHex.toColorOrNull() ?: ElcheColor.Green
    // FASE 8: sustituir por Coil AsyncImage(team.crestUrl) en cuanto haya escudos reales.
    Box(
        modifier = Modifier.size(40.dp).background(color = color, shape = CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = team.shortName.take(1), style = ElcheTheme.typography.label, color = ElcheColor.White)
    }
}

@Preview
@Composable
private fun PredictorCardPreview() {
    ElcheTheme {
        PredictorCard(
            home = Team("elche-cf", "Elche CF", "ELCHE", "", "#05642C"),
            away = Team("rival", "Real Madrid", "R. MADRID", "", "#1B458F"),
            locked = false,
            onSubmit = { _, _ -> },
            modifier = Modifier.padding(ElcheSpacing.lg),
        )
    }
}
