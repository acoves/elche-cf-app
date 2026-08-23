package es.elchecf.app.feature.calendar.players

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheShape
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme
import es.elchecf.app.domain.model.Player
import es.elchecf.app.domain.model.PlayerPosition
import org.koin.compose.viewmodel.koinViewModel

private val positionOrder =
    listOf(
        PlayerPosition.Goalkeeper to "PORTEROS",
        PlayerPosition.Defender to "DEFENSAS",
        PlayerPosition.Midfielder to "CENTROCAMPISTAS",
        PlayerPosition.Forward to "DELANTEROS",
    )

@Composable
fun PlayersRoute() {
    val viewModel = koinViewModel<PlayersViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    PlayersScreen(uiState = uiState)
}

private sealed interface PlayersListItem {
    data class Header(
        val label: String,
    ) : PlayersListItem

    data class Row(
        val left: Player,
        val right: Player?,
    ) : PlayersListItem
}

@Composable
fun PlayersScreen(uiState: PlayersUiState) {
    val squadByPosition = uiState.squad.groupBy { it.position }
    val items =
        positionOrder.flatMap { (position, label) ->
            val players = squadByPosition[position].orEmpty()
            listOf(PlayersListItem.Header(label)) +
                players.chunked(2).map { PlayersListItem.Row(it[0], it.getOrNull(1)) }
        }

    LazyColumn(modifier = Modifier.fillMaxWidth().padding(ElcheSpacing.lg)) {
        items(items) { item ->
            when (item) {
                is PlayersListItem.Header ->
                    Text(
                        text = item.label,
                        style = ElcheTheme.typography.titleM,
                        modifier = Modifier.padding(top = ElcheSpacing.lg, bottom = ElcheSpacing.sm),
                    )
                is PlayersListItem.Row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(ElcheSpacing.md),
                    ) {
                        PlayerCard(item.left, modifier = Modifier.weight(1f))
                        if (item.right != null) {
                            PlayerCard(item.right, modifier = Modifier.weight(1f))
                        } else {
                            Box(modifier = Modifier.weight(1f))
                        }
                    }
            }
        }
    }
}

@Composable
private fun PlayerCard(
    player: Player,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .aspectRatio(1f)
                .clip(ElcheShape.Card)
                .background(ElcheColor.GreenSoft)
                .padding(ElcheSpacing.md),
    ) {
        Text(
            text = player.number.toString(),
            style = ElcheTheme.typography.displayM,
            color = ElcheColor.Green,
        )
        Column(modifier = Modifier.align(Alignment.BottomStart)) {
            Text(text = player.firstName, style = ElcheTheme.typography.bodyS)
            Text(
                text = player.lastName.uppercase(),
                style = ElcheTheme.typography.bodyS,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
