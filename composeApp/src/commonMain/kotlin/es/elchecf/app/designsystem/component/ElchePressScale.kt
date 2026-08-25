package es.elchecf.app.designsystem.component

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheShape
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme

private const val PRESSED_SCALE = 0.94f

/**
 * VISTA PREVIA (temporal, ver Perfil → Configuración → "Vista previa: detalles"): al pulsar, el
 * elemento se encoge un poco y rebota al soltar (spring), en vez de solo el ripple por defecto de
 * Material — sensación más táctil. Pensado para tarjetas y botones destacados, no para toda la
 * app (abusar de esto en cada fila sería ruido). No sustituye a ninguna interacción real todavía.
 */
fun Modifier.elchePressScale(onClick: () -> Unit): Modifier =
    composed {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()
        val scale by
            animateFloatAsState(
                targetValue = if (isPressed) PRESSED_SCALE else 1f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
                label = "elchePressScale",
            )
        this
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }.clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
    }

@Preview
@Composable
private fun ElchePressScalePreview() {
    ElcheTheme {
        Box(
            modifier =
                Modifier
                    .size(120.dp)
                    .clip(ElcheShape.Card)
                    .background(ElcheColor.Green)
                    .elchePressScale(onClick = {})
                    .padding(ElcheSpacing.md),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = "Pulsa", color = ElcheColor.White, style = ElcheTheme.typography.label)
        }
    }
}
