package es.elchecf.app.feature.home.favoriteplayers

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheShape
import es.elchecf.app.designsystem.theme.ElcheTheme

/**
 * Avatar genérico: sin foto real de jugador (no hay licencia para redistribuir las oficiales),
 * mismo icono para todos tal y como pidió el usuario. [ringColor], si se da, dibuja un aro
 * alrededor — para destacar al capitán en [FavoritePlayersCard].
 */
@Composable
fun FavoritePlayerAvatar(
    size: Dp,
    modifier: Modifier = Modifier,
    ringColor: Color? = null,
) {
    val ringed =
        if (ringColor != null) {
            modifier.border(width = 2.dp, color = ringColor, shape = CircleShape).padding(3.dp)
        } else {
            modifier
        }
    Box(
        modifier = ringed.size(size).clip(CircleShape).background(ElcheColor.GreenSoft),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Filled.Person,
            contentDescription = null,
            tint = ElcheColor.Green,
            modifier = Modifier.size(size * 0.6f),
        )
    }
}

/** Botón secundario en píldora, contorno verde — la referencia usa un estilo "ghost" para las
 * acciones que no son la principal de la tarjeta. */
@Composable
fun FavoriteOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = ElcheShape.Button,
        border = BorderStroke(1.dp, ElcheColor.Green),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = ElcheColor.Green),
    ) {
        Text(text = text, style = ElcheTheme.typography.label)
    }
}
