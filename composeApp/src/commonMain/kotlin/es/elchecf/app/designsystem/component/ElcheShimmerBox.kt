package es.elchecf.app.designsystem.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheShape
import es.elchecf.app.designsystem.theme.ElcheTheme

private const val SHIMMER_ANIMATION_MS = 1100

/**
 * VISTA PREVIA (temporal, ver Perfil → Configuración → "Vista previa: detalles"): placeholder de
 * carga con la forma real del contenido (una fila, una tarjeta…) y un brillo que la recorre, en
 * vez de un hueco en blanco con un spinner al lado — "esqueleto" de carga, como en apps grandes
 * (LinkedIn, YouTube). No sustituye a ningún estado de carga real de la app todavía.
 */
@Composable
fun ElcheShimmerBox(
    modifier: Modifier = Modifier,
    shape: Shape = ElcheShape.Card,
) {
    val transition = rememberInfiniteTransition(label = "elcheShimmer")
    val progress by
        transition.animateFloat(
            initialValue = -1f,
            targetValue = 2f,
            animationSpec =
                infiniteRepeatable(
                    animation = tween(SHIMMER_ANIMATION_MS, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart,
                ),
            label = "elcheShimmerProgress",
        )
    BoxWithConstraints(modifier = modifier.clip(shape)) {
        val widthPx = constraints.maxWidth.toFloat()
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(ElcheColor.Divider, ElcheColor.GreenSoft, ElcheColor.Divider),
                            start = Offset(widthPx * progress - widthPx, 0f),
                            end = Offset(widthPx * progress, 0f),
                        ),
                    ),
        )
    }
}

@Preview
@Composable
private fun ElcheShimmerBoxPreview() {
    ElcheTheme {
        ElcheShimmerBox(modifier = Modifier.fillMaxSize())
    }
}
