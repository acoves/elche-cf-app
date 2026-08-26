package es.elchecf.app.feature.game.datigol

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import es.elchecf.app.designsystem.component.ElcheButton
import es.elchecf.app.designsystem.icon.ElcheCalendarIcon
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme
import es.elchecf.app.feature.game.pixelart.drawPixelScore
import es.elchecf.app.feature.game.pixelart.drawPixelSprite
import es.elchecf.app.feature.game.pixelart.drawPixelText
import kotlin.math.ceil

private val SKY_TOP = Color(0xFF8FD3F4)
private val SKY_BOTTOM = Color(0xFFCDEFFF)
private val STAND_SILHOUETTE = Color(0xFF3E6B52)
private val FLOODLIGHT_COLOR = Color(0xFF2E2A22)

private const val FAR_FACTOR = 0.25f
private const val FAR_PERIOD = 900f
private const val MID_FACTOR = 0.55f
private const val MID_PERIOD = 820f
private const val MAX_FRAME_NANOS = 50_000_000L

private fun mod(
    value: Float,
    period: Float,
): Float = ((value % period) + period) % period

/**
 * "Datigol Jump" — minijuego temporal en Perfil → Extra, estilo Doodle Jump ambientado en un
 * estadio de fútbol en bits: Datigol rebota solo de plataforma en plataforma, el jugador solo
 * controla el movimiento horizontal con las dos flechas de abajo. Puntuación solo en memoria de
 * la partida, sin backend ni ranking online.
 */
@Composable
fun DatigolJumpScreen(onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val density = LocalDensity.current
            val widthPx = with(density) { maxWidth.toPx() }
            val heightPx = with(density) { maxHeight.toPx() }

            var resetCount by remember { mutableIntStateOf(0) }
            val gameState =
                remember(resetCount, widthPx, heightPx) {
                    DatigolGameState(canvasWidth = widthPx, canvasHeight = heightPx)
                }
            val leftPressed = remember(resetCount) { mutableStateOf(false) }
            val rightPressed = remember(resetCount) { mutableStateOf(false) }
            val frameTick = remember(resetCount) { mutableIntStateOf(0) }

            GameLoop(
                gameState = gameState,
                leftPressed = leftPressed,
                rightPressed = rightPressed,
                frameTick = frameTick,
            )

            // Lectura de estado en el cuerpo del composable (no solo dentro del Canvas): esto es
            // lo que hace que el marcador y el "GAME OVER" de más abajo se recompongan en cada
            // fotograma — bucle de juego imperativo, no encaja con el patrón StateFlow del resto
            // de la app.
            val currentTick = frameTick.intValue

            DatigolGameCanvas(gameState = gameState, density = density, frameTick = frameTick)
            ScoreOverlay(score = gameState.score)

            if (gameState.gameOver) {
                GameOverOverlay(score = gameState.score, onRestart = { resetCount++ })
            }

            ArrowControls(
                onLeftPressedChange = { leftPressed.value = it },
                onRightPressedChange = { rightPressed.value = it },
                modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(ElcheSpacing.xl),
            )
        }

        IconButton(onClick = onBack, modifier = Modifier.align(Alignment.TopStart).padding(ElcheSpacing.sm)) {
            Icon(imageVector = ElcheCalendarIcon.Previous, contentDescription = "Volver", tint = Color.White)
        }
    }
}

@Composable
private fun GameLoop(
    gameState: DatigolGameState,
    leftPressed: State<Boolean>,
    rightPressed: State<Boolean>,
    frameTick: MutableIntState,
) {
    LaunchedEffect(gameState) {
        var lastFrameNanos = -1L
        while (true) {
            withFrameNanos { nanos ->
                if (lastFrameNanos < 0) lastFrameNanos = nanos
                val deltaNanos = (nanos - lastFrameNanos).coerceIn(0, MAX_FRAME_NANOS)
                lastFrameNanos = nanos
                // Lee `.value` en cada fotograma (no un Boolean capturado): el efecto solo se
                // relanza si cambia `gameState`, así que las flechas deben leerse frescas aquí.
                val direction =
                    when {
                        leftPressed.value && !rightPressed.value -> -1
                        rightPressed.value && !leftPressed.value -> 1
                        else -> 0
                    }
                gameState.update(deltaNanos / 1_000_000_000f, direction)
            }
            frameTick.intValue++
        }
    }
}

@Composable
private fun DatigolGameCanvas(
    gameState: DatigolGameState,
    density: Density,
    frameTick: MutableIntState,
) {
    val pixelSize = with(density) { 4.dp.toPx() }
    Canvas(modifier = Modifier.fillMaxSize()) {
        // Leer el State DENTRO del draw scope invalida solo el dibujado en cada cambio, sin
        // recomponer el árbol — patrón recomendado de Compose para Canvas animado.
        @Suppress("UNUSED_EXPRESSION")
        frameTick.intValue

        drawRect(brush = Brush.verticalGradient(colors = listOf(SKY_TOP, SKY_BOTTOM)), size = size)

        drawFarLayer(gameState.cameraY, size)
        drawMidLayer(gameState.cameraY, size, pixelSize)

        for (platform in gameState.platforms) {
            val screenY = platform.y - gameState.cameraY
            if (screenY < -40f || screenY > size.height + 40f) continue
            drawPixelSprite(
                sprite = platformSprite(),
                topLeft = Offset(platform.x, screenY),
                pixelSize = platform.width / 10f,
            )
        }

        val playerScreenY = gameState.playerY - gameState.cameraY
        val sprite =
            when (gameState.pose) {
                DatigolPose.Idle -> datigolIdleSprite()
                DatigolPose.Jump -> datigolJumpSprite()
                DatigolPose.RunLeft, DatigolPose.RunRight -> datigolRunSprite()
            }
        drawPixelSprite(
            sprite = sprite,
            topLeft = Offset(gameState.playerX, playerScreenY),
            pixelSize = DatigolGameState.PLAYER_SIZE / 18f,
            flipX = gameState.pose == DatigolPose.RunLeft,
        )
    }
}

private fun DrawScope.drawFarLayer(
    cameraY: Float,
    canvasSize: Size,
) {
    val phase = mod(-cameraY * FAR_FACTOR, FAR_PERIOD)
    val tiles = ceil(canvasSize.height / FAR_PERIOD).toInt() + 2
    for (k in -1..tiles) {
        val baseY = phase - FAR_PERIOD + k * FAR_PERIOD
        drawRect(
            color = STAND_SILHOUETTE,
            topLeft = Offset(0f, baseY + FAR_PERIOD * 0.7f),
            size = Size(canvasSize.width, FAR_PERIOD * 0.1f),
        )
        drawRect(
            color = FLOODLIGHT_COLOR,
            topLeft = Offset(canvasSize.width * 0.12f, baseY),
            size = Size(canvasSize.width * 0.025f, FAR_PERIOD * 0.55f),
        )
        drawRect(
            color = FLOODLIGHT_COLOR,
            topLeft = Offset(canvasSize.width * 0.1f, baseY - 14f),
            size = Size(canvasSize.width * 0.065f, 14f),
        )
        drawRect(
            color = FLOODLIGHT_COLOR,
            topLeft = Offset(canvasSize.width * 0.85f, baseY + FAR_PERIOD * 0.15f),
            size = Size(canvasSize.width * 0.025f, FAR_PERIOD * 0.4f),
        )
        drawRect(
            color = FLOODLIGHT_COLOR,
            topLeft = Offset(canvasSize.width * 0.83f, baseY + FAR_PERIOD * 0.15f - 14f),
            size = Size(canvasSize.width * 0.065f, 14f),
        )
    }
}

private fun DrawScope.drawMidLayer(
    cameraY: Float,
    canvasSize: Size,
    pixelSize: Float,
) {
    val phase = mod(-cameraY * MID_FACTOR, MID_PERIOD)
    val tiles = ceil(canvasSize.height / MID_PERIOD).toInt() + 2
    for (k in -1..tiles) {
        val baseY = phase - MID_PERIOD + k * MID_PERIOD
        if (k % 2 == 0) {
            drawPixelSprite(
                sprite = ballSprite(),
                topLeft = Offset(canvasSize.width * 0.72f, baseY),
                pixelSize = pixelSize,
            )
        } else {
            drawPixelText(
                text = "ELCHE",
                topLeft = Offset(canvasSize.width * 0.08f, baseY),
                pixelSize = pixelSize * 0.9f,
                color = Color.White.copy(alpha = 0.75f),
            )
        }
    }
}

@Composable
private fun ScoreOverlay(score: Int) {
    val density = LocalDensity.current
    val pixelSize = with(density) { 5.dp.toPx() }
    Canvas(modifier = Modifier.fillMaxSize()) {
        val text = score.toString().padStart(4, '0')
        drawPixelScore(
            text = text,
            topLeft = Offset(size.width / 2f - (text.length * 6 * pixelSize) / 2f, 32f),
            pixelSize = pixelSize,
            color = Color.White,
        )
    }
}

@Composable
private fun GameOverOverlay(
    score: Int,
    onRestart: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.72f)),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "GAME OVER",
                style = ElcheTheme.typography.displayM,
                color = ElcheColor.Gold,
                fontWeight = FontWeight.Black,
            )
            Text(
                text = "PUNTOS: $score",
                style = ElcheTheme.typography.titleM,
                color = Color.White,
                modifier = Modifier.padding(top = ElcheSpacing.sm, bottom = ElcheSpacing.xl),
            )
            ElcheButton(text = "Volver a intentarlo", onClick = onRestart)
        }
    }
}

@Composable
private fun ArrowControls(
    onLeftPressedChange: (Boolean) -> Unit,
    onRightPressedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "MUEVE A DATIGOL",
            style = ElcheTheme.typography.label,
            color = Color.White,
            modifier = Modifier.padding(bottom = ElcheSpacing.sm),
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            ArrowButton(icon = ElcheCalendarIcon.Previous, onPressedChange = onLeftPressedChange)
            ArrowButton(icon = ElcheCalendarIcon.Next, onPressedChange = onRightPressedChange)
        }
    }
}

@Composable
private fun ArrowButton(
    icon: ImageVector,
    onPressedChange: (Boolean) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    // Se relanza en cada cambio de `isPressed` (misma técnica que ElchePressScale.kt): evita el
    // cierre "congelado" que tendría un LaunchedEffect(Unit) leyendo un Boolean capturado.
    LaunchedEffect(isPressed) { onPressedChange(isPressed) }

    Box(
        modifier =
            Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(if (isPressed) ElcheColor.GreenDeep else ElcheColor.Green)
                .clickable(interactionSource = interactionSource, indication = null, onClick = {}),
        contentAlignment = Alignment.Center,
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = ElcheColor.Gold)
    }
}
