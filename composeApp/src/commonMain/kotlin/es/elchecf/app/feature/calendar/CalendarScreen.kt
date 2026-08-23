package es.elchecf.app.feature.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import es.elchecf.app.designsystem.theme.ElcheTheme
import es.elchecf.app.feature.calendar.players.PlayersRoute
import es.elchecf.app.feature.calendar.standings.StandingsRoute

private enum class CalendarTab(
    val label: String,
) {
    Calendario("Calendario"),
    Clasificaciones("Clasificaciones"),
    Jugadores("Jugadores"),
}

// FASE 5: selector de equipo (Primer equipo/Femenino/Ilicitano) en bottom sheet queda pendiente —
// solo hay datos de ejemplo del primer equipo por ahora.
@Composable
fun CalendarScreen(modifier: Modifier = Modifier) {
    var selectedTab by remember { mutableStateOf(CalendarTab.Calendario) }

    Column(modifier = modifier.fillMaxSize()) {
        // FASE 5: indicador de tab activo con el franja de 6dp (CLAUDE.md §4.4) queda pendiente —
        // la API de indicador personalizado de TabRow cambió entre versiones de Material3 y no se
        // resolvió a tiempo; de momento usa el indicador por defecto (ya en verde, vía `primary`).
        TabRow(
            selectedTabIndex = selectedTab.ordinal,
            containerColor = MaterialTheme.colorScheme.background,
        ) {
            CalendarTab.entries.forEach { tab ->
                Tab(
                    selected = tab == selectedTab,
                    onClick = { selectedTab = tab },
                    text = { Text(text = tab.label, style = ElcheTheme.typography.label) },
                )
            }
        }
        when (selectedTab) {
            CalendarTab.Calendario -> MonthlyCalendarRoute()
            CalendarTab.Clasificaciones -> StandingsRoute()
            CalendarTab.Jugadores -> PlayersRoute()
        }
    }
}
