package es.elchecf.app.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import es.elchecf.app.designsystem.component.ElcheButton
import es.elchecf.app.designsystem.component.ElcheCard
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme

/** "El Quiz del Partido" (CLAUDE.md §5.1): tarjeta con puntuación y CTA. Preguntas reales: fuera de alcance por ahora. */
@Composable
fun QuizCard(
    score: Pair<Int, Int>?,
    onPlayClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ElcheCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "El Quiz del Partido", style = ElcheTheme.typography.titleM)
            Text(
                text = score?.let { "${it.first}/${it.second}" } ?: "—",
                style = ElcheTheme.typography.titleM,
            )
        }
        ElcheButton(
            text = if (score == null) "Jugar" else "Volver a jugar",
            onClick = onPlayClick,
            modifier = Modifier.fillMaxWidth().padding(top = ElcheSpacing.md),
        )
    }
}

@Preview
@Composable
private fun QuizCardPreview() {
    ElcheTheme {
        QuizCard(score = 3 to 5, onPlayClick = {}, modifier = Modifier.padding(ElcheSpacing.lg))
    }
}
