package es.elchecf.app.feature.calendar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import es.elchecf.app.designsystem.component.ElcheSegmentedTabRow
import es.elchecf.app.designsystem.icon.ElcheCalendarIcon
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme
import es.elchecf.app.domain.model.ClubTeam
import es.elchecf.app.feature.calendar.players.PlayersRoute
import es.elchecf.app.feature.calendar.standings.StandingsRoute

private enum class CalendarTab(
    val label: String,
) {
    Calendario("Calendario"),
    Clasificaciones("Clasificaciones"),
    Jugadores("Jugadores"),
}

@Composable
fun CalendarScreen(
    initialTeam: ClubTeam? = null,
    onInitialTeamConsumed: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var selectedTab by remember { mutableStateOf(CalendarTab.Calendario) }
    var selectedTeam by remember { mutableStateOf(initialTeam ?: ClubTeam.PrimerEquipo) }
    var showTeamPicker by remember { mutableStateOf(false) }

    LaunchedEffect(initialTeam) {
        if (initialTeam != null) {
            selectedTeam = initialTeam
            onInitialTeamConsumed()
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        TeamSelectorRow(
            selectedTeam = selectedTeam,
            onClick = { showTeamPicker = true },
            modifier = Modifier.padding(top = ElcheSpacing.sm, start = ElcheSpacing.lg, end = ElcheSpacing.lg),
        )

        ElcheSegmentedTabRow(
            tabs = CalendarTab.entries,
            selected = selectedTab,
            onSelect = { selectedTab = it },
            label = { it.label },
            modifier =
                Modifier.padding(
                    top = ElcheSpacing.sm,
                    start = ElcheSpacing.lg,
                    end = ElcheSpacing.lg,
                    bottom = ElcheSpacing.xs,
                ),
        )
        when (selectedTab) {
            CalendarTab.Calendario -> MonthlyCalendarRoute(selectedTeam = selectedTeam)
            CalendarTab.Clasificaciones -> StandingsRoute(selectedTeam = selectedTeam)
            // FASE 5: Jugadores se queda con el primer equipo por ahora — no pedido para el
            // selector todavía, y el plantel de ejemplo no está separado por equipo.
            CalendarTab.Jugadores -> PlayersRoute()
        }
    }

    if (showTeamPicker) {
        TeamPickerSheet(
            selected = selectedTeam,
            onSelect = {
                selectedTeam = it
                showTeamPicker = false
            },
            onDismiss = { showTeamPicker = false },
        )
    }
}

@Composable
private fun TeamSelectorRow(
    selectedTeam: ClubTeam,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = selectedTeam.label.uppercase(), style = ElcheTheme.typography.titleM)
        Icon(
            imageVector = ElcheCalendarIcon.ExpandMore,
            contentDescription = "Cambiar de equipo",
            tint = ElcheColor.InkMuted,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TeamPickerSheet(
    selected: ClubTeam,
    onSelect: (ClubTeam) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(modifier = Modifier.fillMaxWidth().padding(bottom = ElcheSpacing.xl)) {
            ClubTeam.entries.forEach { team ->
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(team) }
                            .padding(horizontal = ElcheSpacing.lg, vertical = ElcheSpacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = team.label,
                        style = ElcheTheme.typography.body,
                        color = if (team == selected) ElcheColor.Green else ElcheColor.Ink,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}
