package es.elchecf.app.feature.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import es.elchecf.app.designsystem.component.ElcheButton
import es.elchecf.app.designsystem.component.ElcheCard
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme

/** "Valora el partido" (CLAUDE.md §5.1): solo visible si hay partido finalizado en las últimas 48h — lo decide quien llama. */
@Composable
fun ValorateMatchCard(
    onValorarClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ElcheCard(modifier = modifier) {
        Text(text = "¿Qué te ha parecido el partido?", style = ElcheTheme.typography.titleM)
        ElcheButton(
            text = "Valorar partido",
            onClick = onValorarClick,
            modifier = Modifier.fillMaxWidth().padding(top = ElcheSpacing.md),
        )
    }
}

@Preview
@Composable
private fun ValorateMatchCardPreview() {
    ElcheTheme {
        ValorateMatchCard(onValorarClick = {}, modifier = Modifier.padding(ElcheSpacing.lg))
    }
}
