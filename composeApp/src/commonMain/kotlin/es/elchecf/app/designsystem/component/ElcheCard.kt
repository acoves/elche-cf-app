package es.elchecf.app.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheShape
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme

/**
 * Tarjeta base: sombra mínima, la separación viene del borde sutil y el espacio, no de
 * elevación (CLAUDE.md §4.3).
 */
@Composable
fun ElcheCard(
    modifier: Modifier = Modifier,
    shape: Shape = ElcheShape.Card,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier,
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, ElcheColor.Divider),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(ElcheSpacing.lg), content = content)
    }
}

@Preview
@Composable
private fun ElcheCardPreview() {
    ElcheTheme {
        ElcheCard(modifier = Modifier.padding(ElcheSpacing.lg)) {
            Text(text = "Ficha del partido", style = ElcheTheme.typography.titleM)
            Text(text = "Elche CF — Fase 2", style = ElcheTheme.typography.bodyS)
        }
    }
}
