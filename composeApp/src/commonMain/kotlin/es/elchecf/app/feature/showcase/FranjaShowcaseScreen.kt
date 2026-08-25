package es.elchecf.app.feature.showcase

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import es.elchecf.app.designsystem.component.ElcheButton
import es.elchecf.app.designsystem.component.ElcheCard
import es.elchecf.app.designsystem.component.FranjaCelebration
import es.elchecf.app.designsystem.component.FranjaLoadingIndicator
import es.elchecf.app.designsystem.component.FranjaWipeContent
import es.elchecf.app.designsystem.component.SectionHeader
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheShape
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme
import es.elchecf.app.feature.profile.ProfileSubScreenHeader
import kotlinx.coroutines.delay

private val DEMO_BOX_HEIGHT = 180.dp
private const val CELEBRATION_AUTO_HIDE_MS = 2600L

/**
 * VISTA PREVIA (temporal — Perfil → Configuración → "Vista previa: nueva franja"): banco de
 * pruebas para ver en el propio dispositivo, sin tocar ninguna pantalla real, las tres ideas de
 * "franja como firma de marca" propuestas: indicador de carga, celebración y transición entre
 * pantallas. Nada de esto está enganchado todavía a una pantalla de verdad — es solo para decidir
 * si se adoptan. Quitar esta pantalla y su entrada en Perfil una vez decidido.
 */
@Composable
fun FranjaShowcaseScreen(onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(ElcheSpacing.screenMargin)) {
        ProfileSubScreenHeader(title = "Vista previa: franja", onBack = onBack)
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(ElcheSpacing.xl),
        ) {
            LoadingDemo()
            CelebrationDemo()
            WipeDemo()
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
