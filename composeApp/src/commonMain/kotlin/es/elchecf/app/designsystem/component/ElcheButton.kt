package es.elchecf.app.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheShape
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme

/**
 * [Accent] es el dorado de marca: como mucho uno visible por pantalla (CLAUDE.md §4.1).
 * El resto de CTAs usan [Primary].
 */
enum class ElcheButtonVariant { Primary, Accent }

@Composable
fun ElcheButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ElcheButtonVariant = ElcheButtonVariant.Primary,
    enabled: Boolean = true,
) {
    val colors =
        when (variant) {
            ElcheButtonVariant.Primary ->
                ButtonDefaults.buttonColors(containerColor = ElcheColor.Green, contentColor = ElcheColor.White)
            ElcheButtonVariant.Accent ->
                ButtonDefaults.buttonColors(containerColor = ElcheColor.Gold, contentColor = ElcheColor.GoldDeep)
        }

    Button(
        onClick = onClick,
        modifier = modifier.heightIn(min = 48.dp), // tamaño mínimo táctil, CLAUDE.md §7
        enabled = enabled,
        shape = ElcheShape.Button,
        colors = colors,
    ) {
        Text(text = text, style = ElcheTheme.typography.label)
    }
}

@Preview
@Composable
private fun ElcheButtonPreview() {
    ElcheTheme {
        Column(
            modifier = Modifier.padding(ElcheSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(ElcheSpacing.sm),
        ) {
            ElcheButton(text = "Ficha del partido", onClick = {})
            ElcheButton(text = "Comprar entradas", onClick = {}, variant = ElcheButtonVariant.Accent)
            ElcheButton(text = "No disponible", onClick = {}, enabled = false)
        }
    }
}
