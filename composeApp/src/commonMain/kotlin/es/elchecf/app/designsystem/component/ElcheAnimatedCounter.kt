package es.elchecf.app.designsystem.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheTheme

private const val COUNTER_ANIMATION_MS = 500

/**
 * VISTA PREVIA (temporal, ver Perfil → Configuración → "Vista previa: detalles"): número que
 * cuenta hasta el valor nuevo en vez de cambiar de golpe — pensado para el marcador en directo,
 * el contador del Quiz o las estadísticas de un partido. No sustituye a ningún número real de
 * la app todavía.
 */
@Composable
fun ElcheAnimatedCounter(
    targetValue: Int,
    modifier: Modifier = Modifier,
    style: TextStyle = ElcheTheme.typography.displayM,
    color: Color = ElcheColor.Ink,
) {
    val animatedValue by
        animateIntAsState(
            targetValue = targetValue,
            animationSpec = tween(COUNTER_ANIMATION_MS, easing = FastOutSlowInEasing),
            label = "elcheAnimatedCounter",
        )
    Text(text = animatedValue.toString(), style = style, color = color, modifier = modifier)
}

@Preview
@Composable
private fun ElcheAnimatedCounterPreview() {
    ElcheTheme {
        ElcheAnimatedCounter(targetValue = 42)
    }
}
