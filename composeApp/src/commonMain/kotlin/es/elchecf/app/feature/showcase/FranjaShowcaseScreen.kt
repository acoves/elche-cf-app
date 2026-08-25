package es.elchecf.app.feature.showcase

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import es.elchecf.app.designsystem.component.ElcheAnimatedCounter
import es.elchecf.app.designsystem.component.ElcheButton
import es.elchecf.app.designsystem.component.ElcheCard
import es.elchecf.app.designsystem.component.ElcheLivePulse
import es.elchecf.app.designsystem.component.ElcheShimmerBox
import es.elchecf.app.designsystem.component.FranjaCelebration
import es.elchecf.app.designsystem.component.FranjaLoadingIndicator
import es.elchecf.app.designsystem.component.FranjaWipeContent
import es.elchecf.app.designsystem.component.SectionHeader
import es.elchecf.app.designsystem.component.elchePressScale
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheShape
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme
import es.elchecf.app.feature.profile.ProfileSubScreenHeader
import kotlinx.coroutines.delay

private val DEMO_BOX_HEIGHT = 180.dp
private const val CELEBRATION_AUTO_HIDE_MS = 2600L

/**
 * VISTA PREVIA (temporal — Perfil → Configuración → "Vista previa: detalles"): banco de pruebas
 * para ver en el propio dispositivo, sin tocar ninguna pantalla real, ideas de detalles visuales
 * y micro-interacciones: la franja como firma de marca (indicador de carga, celebración,
 * transición entre pantallas) más contador animado, esqueleto de carga, insignia "en directo" y
 * tacto al pulsar. Nada de esto está enganchado todavía a una pantalla de verdad — es solo para
 * decidir qué se adopta. Quitar esta pantalla y su entrada en Perfil una vez decidido.
 */
@Composable
fun FranjaShowcaseScreen(onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(ElcheSpacing.screenMargin)) {
        ProfileSubScreenHeader(title = "Vista previa: detalles", onBack = onBack)
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(ElcheSpacing.xl),
        ) {
            LoadingDemo()
            CelebrationDemo()
            WipeDemo()
            CounterDemo()
            ShimmerDemo()
            LivePulseDemo()
            PressScaleDemo()
        }
    }
}

@Composable
private fun LoadingDemo() {
    Column {
        SectionHeader(title = "Indicador de carga")
        Text(
            text =
                "Sustituiría al círculo de carga genérico en pantallas como Tienda: franjas " +
                    "verde/dorado que suben y bajan en cascada, como las rayas de la camiseta.",
            style = ElcheTheme.typography.bodyS,
            color = ElcheColor.InkMuted,
            modifier = Modifier.padding(top = ElcheSpacing.xs, bottom = ElcheSpacing.md),
        )
        ElcheCard(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier.fillMaxWidth().height(DEMO_BOX_HEIGHT),
                contentAlignment = Alignment.Center,
            ) {
                FranjaLoadingIndicator()
            }
        }
    }
}

@Composable
private fun CelebrationDemo() {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(visible) {
        if (visible) {
            delay(CELEBRATION_AUTO_HIDE_MS)
            visible = false
        }
    }
    Column {
        SectionHeader(title = "Celebración de gol o victoria")
        Text(
            text =
                "Overlay que se dispararía al marcar gol o ganar el partido: franjas que se " +
                    "despliegan en cascada y el mensaje aparece al final, todo se desvanece solo.",
            style = ElcheTheme.typography.bodyS,
            color = ElcheColor.InkMuted,
            modifier = Modifier.padding(top = ElcheSpacing.xs, bottom = ElcheSpacing.md),
        )
        ElcheCard(modifier = Modifier.fillMaxWidth()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(DEMO_BOX_HEIGHT)
                            .clip(ElcheShape.Card)
                            .background(ElcheColor.GreenSoft),
                ) {
                    FranjaCelebration(visible = visible, modifier = Modifier.fillMaxSize())
                }
                ElcheButton(
                    text = "Simular gol",
                    onClick = { visible = true },
                    modifier = Modifier.padding(top = ElcheSpacing.md),
                )
            }
        }
    }
}

@Composable
private fun WipeDemo() {
    var screenA by remember { mutableStateOf(true) }
    Column {
        SectionHeader(title = "Transición entre pantallas (wipe)")
        Text(
            text =
                "Sustituiría al fundido (fade) actual entre pestañas: una franja barre la " +
                    "pantalla y el contenido nuevo aparece justo cuando la cubre por completo.",
            style = ElcheTheme.typography.bodyS,
            color = ElcheColor.InkMuted,
            modifier = Modifier.padding(top = ElcheSpacing.xs, bottom = ElcheSpacing.md),
        )
        ElcheCard(modifier = Modifier.fillMaxWidth()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                FranjaWipeContent(
                    targetState = screenA,
                    modifier = Modifier.fillMaxWidth().height(DEMO_BOX_HEIGHT).clip(ElcheShape.Card),
                ) { isA ->
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .background(if (isA) ElcheColor.Green else ElcheColor.CrestBlue),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = if (isA) "PANTALLA A" else "PANTALLA B",
                            style = ElcheTheme.typography.titleM,
                            color = ElcheColor.White,
                        )
                    }
                }
                ElcheButton(
                    text = "Cambiar de pantalla",
                    onClick = { screenA = !screenA },
                    modifier = Modifier.padding(top = ElcheSpacing.md, bottom = ElcheSpacing.sm),
                )
            }
        }
    }
}

@Composable
private fun CounterDemo() {
    var value by remember { mutableStateOf(0) }
    Column {
        SectionHeader(title = "Contador animado")
        Text(
            text =
                "Para el marcador en directo o el resultado del Quiz: el número sube en vez " +
                    "de cambiar de golpe, se nota más el cambio.",
            style = ElcheTheme.typography.bodyS,
            color = ElcheColor.InkMuted,
            modifier = Modifier.padding(top = ElcheSpacing.xs, bottom = ElcheSpacing.md),
        )
        ElcheCard(modifier = Modifier.fillMaxWidth()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ElcheAnimatedCounter(targetValue = value, modifier = Modifier.padding(vertical = ElcheSpacing.lg))
                ElcheButton(text = "Sumar gol", onClick = { value++ })
            }
        }
    }
}

@Composable
private fun ShimmerDemo() {
    Column {
        SectionHeader(title = "Esqueleto de carga")
        Text(
            text =
                "Placeholder con la forma real del contenido (avatar + dos líneas) y un " +
                    "brillo que lo recorre, en vez de un hueco vacío con un spinner al lado.",
            style = ElcheTheme.typography.bodyS,
            color = ElcheColor.InkMuted,
            modifier = Modifier.padding(top = ElcheSpacing.xs, bottom = ElcheSpacing.md),
        )
        ElcheCard(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ElcheShimmerBox(modifier = Modifier.size(56.dp), shape = CircleShape)
                Column(
                    modifier = Modifier.padding(start = ElcheSpacing.md).weight(1f),
                    verticalArrangement = Arrangement.spacedBy(ElcheSpacing.sm),
                ) {
                    ElcheShimmerBox(modifier = Modifier.fillMaxWidth(0.7f).height(14.dp))
                    ElcheShimmerBox(modifier = Modifier.fillMaxWidth(0.4f).height(14.dp))
                }
            }
        }
    }
}

@Composable
private fun LivePulseDemo() {
    Column {
        SectionHeader(title = "Insignia \"en directo\"")
        Text(
            text =
                "Para el marcador de \"Para ti\" cuando el partido está en juego: un anillo " +
                    "que late alrededor del punto, en vez de un punto fijo.",
            style = ElcheTheme.typography.bodyS,
            color = ElcheColor.InkMuted,
            modifier = Modifier.padding(top = ElcheSpacing.xs, bottom = ElcheSpacing.md),
        )
        ElcheCard(modifier = Modifier.fillMaxWidth()) {
            ElcheLivePulse()
        }
    }
}

@Composable
private fun PressScaleDemo() {
    Column {
        SectionHeader(title = "Tacto al pulsar")
        Text(
            text =
                "La tarjeta se encoge un poco al pulsar y rebota al soltar, en vez del ripple " +
                    "plano de siempre — pensado para elementos destacados, no para toda la app.",
            style = ElcheTheme.typography.bodyS,
            color = ElcheColor.InkMuted,
            modifier = Modifier.padding(top = ElcheSpacing.xs, bottom = ElcheSpacing.md),
        )
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .clip(ElcheShape.Card)
                    .background(ElcheColor.Green)
                    .elchePressScale(onClick = {}),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = "MANTÉN PULSADO", style = ElcheTheme.typography.label, color = ElcheColor.White)
        }
    }
}
