package es.elchecf.app.designsystem.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.StartOffsetType
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheTheme

private val BAR_WIDTH = 6.dp
private val BAR_MIN_HEIGHT = 8.dp
private val BAR_MAX_HEIGHT = 32.dp
private const val BAR_ANIMATION_MS = 500
private const val BAR_STAGGER_MS = 120

/**
 * VISTA PREVIA (temporal, ver Perfil → Configuración → "Vista previa: nueva franja"): indicador
 * de carga propio a partir de la franja de marca (CLAUDE.md §4.4), pensado para sustituir al
 * `CircularProgressIndicator` genérico de Material en las pantallas con "Cargando…" — franjas
 * verticales verde/dorado que suben y bajan en cascada, evocando las rayas de la camiseta.
 * Todavía no sustituye a ningún indicador real de la app.
 */
@Composable
fun FranjaLoadingIndicator(
    modifier: Modifier = Modifier,
    barColors: List<Color> = listOf(ElcheColor.Green, ElcheColor.Gold, ElcheColor.Green, ElcheColor.Gold),
) {
    val transition = rememberInfiniteTransition(label = "franjaLoading")
    Row(
        modifier = modifier.height(BAR_MAX_HEIGHT),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.Bottom,
    ) {
        barColors.forEachIndexed { index, color ->
            val height by
                transition.animateFloat(
                    initialValue = BAR_MIN_HEIGHT.value,
                    targetValue = BAR_MAX_HEIGHT.value,
                    animationSpec =
                        infiniteRepeatable(
                            animation = tween(BAR_ANIMATION_MS, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse,
                            initialStartOffset = StartOffset(index * BAR_STAGGER_MS, StartOffsetType.Delay),
                        ),
                    label = "franjaLoadingBar$index",
                )
            Box(
                modifier =
                    Modifier
                        .width(BAR_WIDTH)
                        .height(height.dp)
                        .clip(RoundedCornerShape(BAR_WIDTH / 2))
                        .background(color),
            )
        }
    }
}

@Preview
@Composable
private fun FranjaLoadingIndicatorPreview() {
    ElcheTheme {
        FranjaLoadingIndicator()
    }
}
