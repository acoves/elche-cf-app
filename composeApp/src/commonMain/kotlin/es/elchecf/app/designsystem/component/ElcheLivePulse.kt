package es.elchecf.app.designsystem.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme

private val DOT_SIZE = 8.dp
private const val PULSE_ANIMATION_MS = 1200
private const val PULSE_MAX_SCALE = 2.4f

/**
 * VISTA PREVIA (temporal, ver Perfil → Configuración → "Vista previa: detalles"): insignia de
 * "en directo" con un anillo que late alrededor del punto, en vez de un punto fijo — pensado para
 * el marcador de "Para ti" cuando el partido está en curso. No sustituye a ninguna insignia real
 * de la app todavía.
 */
@Composable
fun ElcheLivePulse(
    modifier: Modifier = Modifier,
    color: Color = ElcheColor.CrestRed,
    label: String = "EN DIRECTO",
) {
    val transition = rememberInfiniteTransition(label = "elcheLivePulse")
    val scale by
        transition.animateFloat(
            initialValue = 1f,
            targetValue = PULSE_MAX_SCALE,
            animationSpec =
                infiniteRepeatable(tween(PULSE_ANIMATION_MS, easing = LinearEasing), repeatMode = RepeatMode.Restart),
            label = "elcheLivePulseScale",
        )
    val ringAlpha by
        transition.animateFloat(
            initialValue = 0.6f,
            targetValue = 0f,
            animationSpec =
                infiniteRepeatable(tween(PULSE_ANIMATION_MS, easing = LinearEasing), repeatMode = RepeatMode.Restart),
            label = "elcheLivePulseAlpha",
        )

    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Box(modifier = Modifier.size(DOT_SIZE * PULSE_MAX_SCALE), contentAlignment = Alignment.Center) {
            Box(
                modifier =
                    Modifier
                        .size(DOT_SIZE)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            alpha = ringAlpha
                        }.background(color, CircleShape),
            )
            Box(modifier = Modifier.size(DOT_SIZE).background(color, CircleShape))
        }
        Text(
            text = label,
            style = ElcheTheme.typography.label,
            color = color,
            modifier = Modifier.padding(start = ElcheSpacing.xs),
        )
    }
}

@Preview
@Composable
private fun ElcheLivePulsePreview() {
    ElcheTheme {
        ElcheLivePulse()
    }
}
