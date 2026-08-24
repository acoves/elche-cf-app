package es.elchecf.app.feature.calendar.standings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme
import es.elchecf.app.domain.model.ClubTeam
import es.elchecf.app.domain.model.StandingRow
import es.elchecf.app.domain.model.Team
import es.elchecf.app.domain.repository.Competition
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StandingsRoute(selectedTeam: ClubTeam) {
    val viewModel = koinViewModel<StandingsViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(selectedTeam) { viewModel.selectTeam(selectedTeam) }
    StandingsScreen(uiState = uiState, onSelectCompetition = viewModel::selectCompetition)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandingsScreen(
    uiState: StandingsUiState,
    onSelectCompetition: (Competition) -> Unit = {},
) {
    val competitions = listOf(Competition.LaLiga to "LALIGA", Competition.Copa to "COPA")

    Box(modifier = Modifier.fillMaxWidth()) {
        Column {
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth().padding(ElcheSpacing.lg)) {
                competitions.forEachIndexed { index, (competition, label) ->
                    SegmentedButton(
                        selected = uiState.selectedCompetition == competition,
                        onClick = { onSelectCompetition(competition) },
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = competitions.size),
                    ) {
                        Text(text = label, style = ElcheTheme.typography.label)
                    }
                }
            }

            if (uiState.error != null) {
                Text(
                    text = uiState.error,
                    style = ElcheTheme.typography.bodyS,
                    modifier = Modifier.padding(horizontal = ElcheSpacing.lg),
                )
            }

            when (uiState.selectedCompetition) {
                Competition.LaLiga -> StandingsTable(uiState.standings)
                Competition.Copa -> CupBracketView(uiState.cupBracket)
            }
        }
    }
}

@Composable
private fun StandingsTable(rows: List<StandingRow>) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = ElcheSpacing.lg)) {
        StandingsHeaderRow()
        rows.forEach { row -> StandingsTableRow(row) }
    }
}

@Composable
private fun StandingsHeaderRow() {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = ElcheSpacing.sm)) {
        Text(
            text = "#",
            style = ElcheTheme.typography.label,
            color = ElcheColor.InkMuted,
            modifier = Modifier.width(32.dp),
        )
        Text(
            text = "EQUIPO",
            style = ElcheTheme.typography.label,
            color = ElcheColor.InkMuted,
            modifier = Modifier.weight(1f).padding(start = ElcheSpacing.sm),
        )
        listOf("J", "V", "E", "D", "DG", "PTS").forEach { column ->
            StatCell(text = column, color = ElcheColor.InkMuted)
        }
    }
}

@Composable
private fun StandingsTableRow(row: StandingRow) {
    val isElche = row.team.id == Team.ELCHE_ID
    val zoneColor =
        when {
            row.position <= EUROPE_ZONE_LAST_POSITION -> ElcheColor.CrestBlue
            row.position > RELEGATION_ZONE_FIRST_POSITION -> ElcheColor.CrestRed
            else -> null
        }

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(if (isElche) ElcheColor.GreenSoft else ElcheColor.White)
                .padding(vertical = ElcheSpacing.sm),
    ) {
        Box(
            modifier =
                Modifier
                    .width(3.dp)
                    .background((if (isElche) ElcheColor.Green else zoneColor) ?: ElcheColor.White),
        )
        Text(
            text = row.position.toString(),
            style = ElcheTheme.typography.bodyS,
            modifier = Modifier.width(32.dp).padding(start = ElcheSpacing.sm),
        )
        Text(
            text = row.team.shortName,
            style = ElcheTheme.typography.bodyS,
            fontWeight = if (isElche) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.weight(1f).padding(start = ElcheSpacing.sm),
        )
        StatCell(text = row.played.toString())
        StatCell(text = row.won.toString())
        StatCell(text = row.drawn.toString())
        StatCell(text = row.lost.toString())
        StatCell(text = row.goalDiff.toString())
        StatCell(text = row.points.toString(), bold = true)
    }
}

@Composable
private fun StatCell(
    text: String,
    color: Color = ElcheColor.Ink,
    bold: Boolean = false,
) {
    Text(
        text = text,
        style = ElcheTheme.typography.bodyS,
        color = color,
        fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
        textAlign = TextAlign.Center,
        modifier = Modifier.width(32.dp),
    )
}

private const val EUROPE_ZONE_LAST_POSITION = 4
private const val RELEGATION_ZONE_FIRST_POSITION = 17
