package es.elchecf.app.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheTheme
import kotlinx.coroutines.delay

private val STRIPE_HEIGHT = 28.dp
private const val STRIPE_STAGGER_MS = 80L
private const val STRIPE_ANIMATION_MS = 350

/**
 * VISTA PREVIA (temporal, ver Perfil → Configuración → "Vista previa: nueva franja"): overlay de
 * celebración a partir de la franja de marca — pensado para un gol o una victoria del Elche, en
 * vez de un simple cambio de marcador. Las franjas se despliegan en cascada de izquierda a
 * derecha y el mensaje aparece al final; todo se desvanece solo. No está enganchado todavía a
 * ningún evento real (gol/victoria) de la app.
 */
@Composable
fun FranjaCelebration(
    visible: Boolean,
    modifier: Modifier = Modifier,
    message: String = "¡GOOOL!",
    stripeColors: List<Color> = listOf(ElcheColor.Green, ElcheColor.Gold, ElcheColor.Green),
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(150)),
        exit = fadeOut(tween(300)),
        modifier = modifier,
    ) {
        Box(modifier = Modifier.fillMaxSize().background(ElcheColor.Ink.copy(alpha = 0.55f))) {
            Column(
                modifier = Modifier.align(Alignment.Center).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                stripeColors.forEachIndexed { index, color ->
                    val widthFraction = remember(visible) { Animatable(0f) }
                    LaunchedEffect(visible) {
                        if (visible) {
                            delay(index * STRIPE_STAGGER_MS)
                            widthFraction.animateTo(1f, tween(STRIPE_ANIMATION_MS, easing = FastOutSlowInEasing))
                        }
                    }
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth(widthFraction.value)
                                .height(STRIPE_HEIGHT)
                                .background(color),
                    )
                }
                var messageVisible by remember(visible) { mutableStateOf(false) }
                LaunchedEffect(visible) {
                    if (visible) {
                        delay(stripeColors.size * STRIPE_STAGGER_MS + STRIPE_ANIMATION_MS)
                        messageVisible = true
                    } else {
                        messageVisible = false
                    }
                }
                AnimatedVisibility(visible = messageVisible, enter = fadeIn(tween(200))) {
                    Text(
                        text = message,
                        style = ElcheTheme.typography.displayL,
                        color = ElcheColor.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun FranjaCelebrationPreview() {
    ElcheTheme {
        FranjaCelebration(visible = true)
    }
}
