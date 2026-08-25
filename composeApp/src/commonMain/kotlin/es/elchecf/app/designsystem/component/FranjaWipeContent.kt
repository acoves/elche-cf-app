package es.elchecf.app.designsystem.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheTheme

private const val WIPE_DURATION_MS = 320

/**
 * VISTA PREVIA (temporal, ver Perfil → Configuración → "Vista previa: nueva franja"): transición
 * entre dos contenidos con una franja de marca que barre la pantalla, en vez del fundido (fade)
 * que usa hoy `RootNavHost` entre pestañas (CLAUDE.md §12, [ElcheTheme]). El contenido nuevo se
 * revela justo cuando la franja cubre el centro de la pantalla. No sustituye todavía a la
 * transición real de la navegación.
 */
@Composable
fun <T> FranjaWipeContent(
    targetState: T,
    modifier: Modifier = Modifier,
    stripeColor: Color = ElcheColor.Green,
    content: @Composable (T) -> Unit,
) {
    var displayedState by remember { mutableStateOf(targetState) }
    var progress by remember { mutableStateOf(0f) }

    LaunchedEffect(targetState) {
        if (targetState == displayedState) return@LaunchedEffect
        var swapped = false
        animate(0f, 1f, animationSpec = tween(WIPE_DURATION_MS, easing = FastOutSlowInEasing)) { value, _ ->
            progress = value
            if (!swapped && value >= 0.5f) {
                displayedState = targetState
                swapped = true
            }
        }
        progress = 0f
    }

    Box(modifier = modifier) {
        content(displayedState)
        if (progress > 0f && progress < 1f) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .graphicsLayer { translationX = size.width * (progress * 2f - 1f) }
                        .background(stripeColor),
            )
        }
    }
}

@Preview
@Composable
private fun FranjaWipeContentPreview() {
    ElcheTheme {
        FranjaWipeContent(targetState = true, modifier = Modifier.fillMaxSize()) { state ->
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(if (state) ElcheColor.GreenSoft else ElcheColor.Gold.copy(alpha = 0.3f)),
            )
        }
    }
}
