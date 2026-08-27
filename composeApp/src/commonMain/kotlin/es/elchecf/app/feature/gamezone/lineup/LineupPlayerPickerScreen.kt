package es.elchecf.app.feature.gamezone.lineup

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheShape
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme
import es.elchecf.app.feature.home.favoriteplayers.FavoriteFlowHeader
import es.elchecf.app.feature.home.favoriteplayers.FavoritePlayerAvatar
import es.elchecf.app.feature.home.favoriteplayers.PlayerPosition
import es.elchecf.app.feature.home.favoriteplayers.SquadPlayer
import es.elchecf.app.feature.home.favoriteplayers.elcheSquad2627

/**
 * Selector de jugador para un hueco del campo, filtrado a la demarcación de ese hueco (no una
 * lista plana con todo el mundo, como la referencia) — rejilla de tarjetas con avatar, dorsal y
 * nombre; los ya alineados en otro hueco salen atenuados con un check, no se pueden repetir.
 */
@Composable
fun LineupPlayerPickerScreen(
    position: PlayerPosition,
    usedNumbers: Set<Int>,
    onSelect: (SquadPlayer) -> Unit,
    onBack: () -> Unit,
) {
    val players = elcheSquad2627.filter { it.position == position }

    Column(modifier = Modifier.fillMaxSize().background(ElcheColor.Ink)) {
        FavoriteFlowHeader(title = "Elige ${position.label.lowercase()}", onBack = onBack)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(ElcheSpacing.screenMargin),
            horizontalArrangement = Arrangement.spacedBy(ElcheSpacing.md),
            verticalArrangement = Arrangement.spacedBy(ElcheSpacing.md),
            modifier = Modifier.fillMaxSize(),
        ) {
            items(players) { player ->
                val alreadyUsed = player.number in usedNumbers
                PlayerPickerCard(
                    player = player,
                    disabled = alreadyUsed,
                    onClick = { if (!alreadyUsed) onSelect(player) },
                )
            }
        }
    }
}

@Composable
private fun PlayerPickerCard(
    player: SquadPlayer,
    disabled: Boolean,
    onClick: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(ElcheShape.Card)
                .background(Color.White.copy(alpha = if (disabled) 0.03f else 0.07f))
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = if (disabled) 0.08f else 0.18f),
                    shape = ElcheShape.Card,
                ).clickable(enabled = !disabled, onClick = onClick)
                .padding(ElcheSpacing.md),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        FavoritePlayerAvatar(size = 56.dp, ringColor = if (disabled) null else ElcheColor.Green)
        Text(
            text = "${player.number} · ${player.name}",
            style = ElcheTheme.typography.body,
            color = if (disabled) ElcheColor.White.copy(alpha = 0.35f) else ElcheColor.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = ElcheSpacing.sm),
        )
        if (disabled) {
            Row(
                modifier = Modifier.padding(top = ElcheSpacing.xs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = ElcheColor.Gold,
                    modifier = Modifier.size(14.dp),
                )
                Text(
                    text = "Ya en el once",
                    style = ElcheTheme.typography.label,
                    color = ElcheColor.Gold,
                    modifier = Modifier.padding(start = 4.dp),
                )
            }
        }
    }
}
