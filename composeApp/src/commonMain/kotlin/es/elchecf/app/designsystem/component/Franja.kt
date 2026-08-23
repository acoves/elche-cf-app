package es.elchecf.app.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheTheme

/**
 * La franja: elemento firma del franjiverde (CLAUDE.md §4.4). Banda horizontal de color,
 * usada como indicador de tab activo, divisor de sección y fondo partido de [VersusCard]
 * (Fase 4). No incluye la variante animada de indicador de carga — eso llega cuando haga
 * falta un loading real (Fase 4+).
 */
@Composable
fun Franja(
    modifier: Modifier = Modifier,
    color: Color = ElcheColor.Green,
    thickness: Dp = 6.dp,
) {
    Box(modifier = modifier.height(thickness).background(color))
}

@Preview
@Composable
private fun FranjaPreview() {
    ElcheTheme {
        Box {
            Franja(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Preview
@Composable
private fun FranjaShortPreview() {
    ElcheTheme {
        Franja(modifier = Modifier.width(48.dp), color = ElcheColor.Gold)
    }
}
