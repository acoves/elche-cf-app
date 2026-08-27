package es.elchecf.app.feature.gamezone.lineup

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheShape
import es.elchecf.app.designsystem.theme.ElcheTheme
import es.elchecf.app.feature.home.favoriteplayers.FavoritePlayerAvatar
import es.elchecf.app.feature.home.favoriteplayers.PlayerPosition
import es.elchecf.app.feature.home.favoriteplayers.SquadPlayer

private val SLOT_WIDTH = 76.dp
private val AVATAR_SIZE = 44.dp
private val LINE_COLOR = Color.White.copy(alpha = 0.45f)

/** Campo con las marcas dibujadas a mano (Canvas) y un hueco interactivo por posición de
 * [formation]: vacío = "+" con la demarcación, ocupado = avatar + dorsal + nombre — los avatares
 * son genéricos (sin foto real de cada jugador), así que el nombre es lo único que distingue un
 * hueco de otro. */
@Composable
fun LineupPitch(
    formation: Formation,
    assignments: Map<Int, SquadPlayer>,
    onSlotClick: (Int) -> Unit,
    onSlotClear: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) { drawPitchMarkings() }
        formation.slots.forEachIndexed { index, slot ->
            LineupSlotChip(
                position = slot.position,
                player = assignments[index],
                onClick = { onSlotClick(index) },
                onClear = { onSlotClear(index) },
                modifier =
                    Modifier
                        .align(Alignment.TopStart)
                        .offset(x = maxWidth * slot.x - SLOT_WIDTH / 2, y = maxHeight * slot.y - AVATAR_SIZE / 2),
            )
        }
    }
}

private fun DrawScope.drawPitchMarkings() {
    val w = size.width
    val h = size.height
    val strokeWidth = 2.dp.toPx()

    // Área y semicírculo de la portería propia, arriba.
    val boxWidth = w * 0.62f
    val boxHeight = h * 0.14f
    drawRect(
        color = LINE_COLOR,
        topLeft = Offset((w - boxWidth) / 2f, 0f),
        size = Size(boxWidth, boxHeight),
        style = Stroke(width = strokeWidth),
    )
    val arcSize = h * 0.18f
    drawArc(
        color = LINE_COLOR,
        startAngle = 20f,
        sweepAngle = 140f,
        useCenter = false,
        topLeft = Offset(w / 2f - arcSize / 2f, boxHeight - arcSize * 0.35f),
        size = Size(arcSize, arcSize),
        style = Stroke(width = strokeWidth),
    )

    // Línea de medio campo + círculo central.
    val halfwayY = h * 0.40f
    drawLine(color = LINE_COLOR, start = Offset(0f, halfwayY), end = Offset(w, halfwayY), strokeWidth = strokeWidth)
    drawCircle(
        color = LINE_COLOR,
        radius = h * 0.11f,
        center = Offset(w / 2f, halfwayY),
        style = Stroke(width = strokeWidth),
    )

    // Borde del campo.
    drawRect(color = LINE_COLOR, topLeft = Offset.Zero, size = Size(w, h), style = Stroke(width = strokeWidth))
}

@Composable
private fun LineupSlotChip(
    position: PlayerPosition,
    player: SquadPlayer?,
    onClick: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.width(SLOT_WIDTH),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (player == null) {
            EmptySlot(position = position, onClick = onClick)
        } else {
            FilledSlot(player = player, onClick = onClick, onClear = onClear)
        }
    }
}

@Composable
private fun EmptySlot(
    position: PlayerPosition,
    onClick: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .size(AVATAR_SIZE)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.14f))
                .border(width = 1.5.dp, color = Color.White.copy(alpha = 0.55f), shape = CircleShape)
                .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = position.label, tint = Color.White)
    }
}

@Composable
private fun FilledSlot(
    player: SquadPlayer,
    onClick: () -> Unit,
    onClear: () -> Unit,
) {
    Box(modifier = Modifier.clickable(onClick = onClick)) {
        FavoritePlayerAvatar(size = AVATAR_SIZE, ringColor = ElcheColor.Gold)
        Box(
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 6.dp)
                    .clip(CircleShape)
                    .background(ElcheColor.Green)
                    .padding(horizontal = 5.dp, vertical = 1.dp),
        ) {
            Text(text = player.number.toString(), style = ElcheTheme.typography.label, color = ElcheColor.White)
        }
        Box(
            modifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 4.dp, y = (-4).dp)
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(ElcheColor.Ink)
                    .clickable(onClick = onClear),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Quitar",
                tint = Color.White,
                modifier = Modifier.size(12.dp),
            )
        }
    }
    Box(
        modifier =
            Modifier
                .padding(top = 10.dp)
                .clip(ElcheShape.Pill)
                .background(ElcheColor.Ink.copy(alpha = 0.75f))
                .padding(horizontal = 6.dp, vertical = 2.dp),
    ) {
        Text(
            text = player.name.substringAfterLast(' '),
            style = ElcheTheme.typography.label,
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
