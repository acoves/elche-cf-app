package es.elchecf.app.feature.gamezone

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import es.elchecf.app.designsystem.component.ElcheButton
import es.elchecf.app.designsystem.component.ElcheButtonVariant
import es.elchecf.app.designsystem.icon.ElcheCalendarIcon
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheShape
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme
import es.elchecf.app.feature.game.datigol.DatigolJumpScreen
import es.elchecf.app.feature.game.datigol.datigolIdleSprite
import es.elchecf.app.feature.game.pixelart.drawPixelSprite

private sealed interface GameZoneSubScreen {
    data object Hub : GameZoneSubScreen

    data object DatigolJump : GameZoneSubScreen
}

/**
 * Hub de minijuegos "Game Zone" (se llega desde el banner de "Para ti"): fondo oscuro con
 * degradado verde Elche, inspirado en el "Activity Hub" de la app del Chelsea. Por ahora Datigol
 * Jump es el único juego real; el resto son tarjetas "muy pronto" honestas, no funcionalidades
 * simuladas.
 */
@Composable
fun GameZoneScreen(onBack: () -> Unit) {
    var subScreen by remember { mutableStateOf<GameZoneSubScreen>(GameZoneSubScreen.Hub) }

    when (subScreen) {
        GameZoneSubScreen.Hub ->
            GameZoneHub(
                onBack = onBack,
                onPlayDatigol = { subScreen = GameZoneSubScreen.DatigolJump },
            )
        GameZoneSubScreen.DatigolJump ->
            DatigolJumpScreen(onBack = { subScreen = GameZoneSubScreen.Hub })
    }
}

@Composable
private fun GameZoneHub(
    onBack: () -> Unit,
    onPlayDatigol: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(ElcheColor.GreenDeep, ElcheColor.Ink))),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(ElcheSpacing.screenMargin),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = ElcheCalendarIcon.Previous,
                contentDescription = "Volver",
                tint = ElcheColor.White,
                modifier = Modifier.clickable(onClick = onBack),
            )
            Text(
                text = "GAME ZONE",
                style = ElcheTheme.typography.titleL,
                color = ElcheColor.White,
                modifier = Modifier.padding(start = ElcheSpacing.md),
            )
        }
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = ElcheSpacing.screenMargin),
        ) {
            Text(
                text = "Ponte a prueba con los juegos del Elche CF",
                style = ElcheTheme.typography.bodyS,
                color = ElcheColor.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = ElcheSpacing.lg),
            )
            DatigolJumpCard(onPlayClick = onPlayDatigol)
            ComingSoonCard(
                title = "Predictor Franjiverde",
                modifier = Modifier.padding(top = ElcheSpacing.md),
            )
            ComingSoonCard(
                title = "Quiz del Elche",
                modifier = Modifier.padding(top = ElcheSpacing.md, bottom = ElcheSpacing.xxl),
            )
        }
    }
}

@Composable
private fun DatigolJumpCard(
    onPlayClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(ElcheShape.CardLarge)
                .background(ElcheColor.Green.copy(alpha = 0.16f))
                .border(width = 1.dp, color = ElcheColor.Green.copy(alpha = 0.5f), shape = ElcheShape.CardLarge)
                .padding(ElcheSpacing.lg),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(72.dp).clip(ElcheShape.Card).background(ElcheColor.Ink),
                contentAlignment = Alignment.Center,
            ) {
                val sprite = remember { datigolIdleSprite() }
                Canvas(modifier = Modifier.size(56.dp)) {
                    val pixelSize = size.minDimension / sprite.heightPx
                    drawPixelSprite(
                        sprite = sprite,
                        topLeft = Offset((size.width - sprite.widthPx * pixelSize) / 2f, 0f),
                        pixelSize = pixelSize,
                    )
                }
            }
            Column(modifier = Modifier.weight(1f).padding(start = ElcheSpacing.md)) {
                Text(text = "DATIGOL JUMP", style = ElcheTheme.typography.titleM, color = ElcheColor.White)
                Text(
                    text = "Salta de plataforma en plataforma con Datigol",
                    style = ElcheTheme.typography.bodyS,
                    color = ElcheColor.White.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = ElcheSpacing.xs),
                )
            }
        }
        ElcheButton(
            text = "Jugar",
            onClick = onPlayClick,
            variant = ElcheButtonVariant.Accent,
            modifier = Modifier.fillMaxWidth().padding(top = ElcheSpacing.lg),
        )
    }
}

/** Tarjeta "muy pronto": sin CTA ni interacción, para no simular una función que no existe. */
@Composable
private fun ComingSoonCard(
    title: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(ElcheShape.CardLarge)
                .background(ElcheColor.White.copy(alpha = 0.06f))
                .padding(ElcheSpacing.lg),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title.uppercase(),
                style = ElcheTheme.typography.titleM,
                color = ElcheColor.White.copy(alpha = 0.5f),
            )
            Text(
                text = "Muy pronto",
                style = ElcheTheme.typography.bodyS,
                color = ElcheColor.White.copy(alpha = 0.4f),
                modifier = Modifier.padding(top = ElcheSpacing.xs),
            )
        }
    }
}
