package es.elchecf.app.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme

/**
 * Cabecera de sección: título en mayúsculas + franja de 6dp como acento, en vez de la línea
 * de 1dp habitual (CLAUDE.md §4.4).
 */
@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    action: @Composable () -> Unit = {},
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = title.uppercase(), style = ElcheTheme.typography.titleM)
            action()
        }
        Spacer(modifier = Modifier.height(ElcheSpacing.xs))
        Franja(modifier = Modifier.width(48.dp))
    }
}

@Preview
@Composable
private fun SectionHeaderPreview() {
    ElcheTheme {
        SectionHeader(title = "Próximo partido", modifier = Modifier.padding(ElcheSpacing.lg))
    }
}
