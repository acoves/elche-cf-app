package es.elchecf.app.feature.home.favoriteplayers

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.unit.dp
import es.elchecf.app.designsystem.component.ElcheButton
import es.elchecf.app.designsystem.component.ElcheButtonVariant
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheShape
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme

private const val MAX_FAVORITES = 5

/**
 * Selección de jugadores favoritos (hasta [MAX_FAVORITES]), estilo oscuro con verde Elche —
 * mismo flujo que "Your favourite players" de la referencia, agrupado por demarcación.
 */
@Composable
fun FavoritePlayersSelectScreen(
    initialSelection: Set<Int>,
    onBack: () -> Unit,
    onContinue: (Set<Int>) -> Unit,
) {
    var selected by remember { mutableStateOf(initialSelection) }

    Column(modifier = Modifier.fillMaxSize().background(ElcheColor.Ink)) {
        FavoriteFlowHeader(title = "Tus jugadores favoritos", onBack = onBack)
        Text(
            text = "¿Quiénes son tus jugadores favoritos? Elige hasta $MAX_FAVORITES para personalizar tu experiencia.",
            style = ElcheTheme.typography.bodyS,
            color = ElcheColor.White.copy(alpha = 0.7f),
            modifier = Modifier.padding(horizontal = ElcheSpacing.screenMargin),
        )
        LazyColumn(
            modifier = Modifier.weight(1f).padding(horizontal = ElcheSpacing.screenMargin),
            contentPadding = PaddingValues(top = ElcheSpacing.lg),
        ) {
            if (selected.isNotEmpty()) {
                item {
                    Text(
                        text = "Seleccionados (${selected.size}/$MAX_FAVORITES)",
                        style = ElcheTheme.typography.titleM,
                        color = ElcheColor.White,
                        modifier = Modifier.padding(bottom = ElcheSpacing.sm),
                    )
                }
                items(elcheSquad2627.filter { it.number in selected }) { player ->
                    PlayerSelectRow(
                        player = player,
                        selected = true,
                        onClick = { selected = selected - player.number },
                        modifier = Modifier.padding(bottom = ElcheSpacing.sm),
                    )
                }
                item { Spacer(modifier = Modifier.height(ElcheSpacing.lg)) }
            }
            PlayerPosition.entries.forEach { position ->
                val players = elcheSquad2627.filter { it.position == position }
                if (players.isNotEmpty()) {
                    item {
                        Text(
                            text = position.label,
                            style = ElcheTheme.typography.titleM,
                            color = ElcheColor.White,
                            modifier = Modifier.padding(bottom = ElcheSpacing.sm),
                        )
                    }
                    items(players) { player ->
                        PlayerSelectRow(
                            player = player,
                            selected = player.number in selected,
                            onClick = {
                                selected =
                                    when {
                                        player.number in selected -> selected - player.number
                                        selected.size < MAX_FAVORITES -> selected + player.number
                                        else -> selected
                                    }
                            },
                            modifier = Modifier.padding(bottom = ElcheSpacing.sm),
                        )
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(ElcheSpacing.lg)) }
        }
        ElcheButton(
            text = "Continuar",
            onClick = { onContinue(selected) },
            enabled = selected.isNotEmpty(),
            variant = ElcheButtonVariant.Accent,
            modifier = Modifier.fillMaxWidth().padding(ElcheSpacing.screenMargin),
        )
    }
}

@Composable
private fun PlayerSelectRow(
    player: SquadPlayer,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(ElcheShape.Card)
                .background(ElcheColor.White.copy(alpha = 0.06f))
                .clickable(onClick = onClick)
                .padding(ElcheSpacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FavoritePlayerAvatar(size = 40.dp)
        Text(
            text = "${player.number} ${player.name}",
            style = ElcheTheme.typography.body,
            color = ElcheColor.White,
            modifier = Modifier.weight(1f).padding(start = ElcheSpacing.md),
        )
        StarSelectionMarker(selected = selected)
    }
}

@Composable
private fun StarSelectionMarker(selected: Boolean) {
    Box(
        modifier =
            Modifier
                .size(28.dp)
                .clip(CircleShape)
                .then(
                    if (selected) {
                        Modifier.background(ElcheColor.Gold)
                    } else {
                        Modifier.border(1.5.dp, ElcheColor.White.copy(alpha = 0.4f), CircleShape)
                    },
                ),
        contentAlignment = Alignment.Center,
    ) {
        if (selected) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = ElcheColor.GoldDeep,
                modifier = Modifier.size(16.dp),
            )
        }
    }
}
