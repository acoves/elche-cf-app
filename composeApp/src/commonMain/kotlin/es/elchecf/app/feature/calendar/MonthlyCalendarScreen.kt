package es.elchecf.app.feature.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import es.elchecf.app.designsystem.icon.ElcheCalendarIcon
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme
import es.elchecf.app.domain.model.Match
import es.elchecf.app.domain.model.Team
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel

private val weekdayLabels = listOf("L", "M", "X", "J", "V", "S", "D")
private val monthNames =
    listOf(
        "ENERO",
        "FEBRERO",
        "MARZO",
        "ABRIL",
        "MAYO",
        "JUNIO",
        "JULIO",
        "AGOSTO",
        "SEPTIEMBRE",
        "OCTUBRE",
        "NOVIEMBRE",
        "DICIEMBRE",
    )

@Composable
fun MonthlyCalendarRoute() {
    val viewModel = koinViewModel<MonthlyCalendarViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    MonthlyCalendarScreen(
        uiState = uiState,
        onPreviousMonth = viewModel::goToPreviousMonth,
        onNextMonth = viewModel::goToNextMonth,
    )
}

@Composable
fun MonthlyCalendarScreen(
    uiState: MonthlyCalendarUiState,
    onPreviousMonth: () -> Unit = {},
    onNextMonth: () -> Unit = {},
) {
    val month = uiState.displayedMonth
    val matchesByDay =
        uiState.matches
            .filter { YearMonth.of(it.kickoffInstant) == month }
            .associateBy { dayOfMonthOf(it) }

    Column(modifier = Modifier.fillMaxWidth().padding(ElcheSpacing.lg)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onPreviousMonth) {
                Icon(imageVector = ElcheCalendarIcon.Previous, contentDescription = "Mes anterior")
            }
            Text(text = "${monthNames[month.month - 1]} ${month.year}", style = ElcheTheme.typography.titleM)
            IconButton(onClick = onNextMonth) {
                Icon(imageVector = ElcheCalendarIcon.Next, contentDescription = "Mes siguiente")
            }
        }

        Row(modifier = Modifier.fillMaxWidth().padding(top = ElcheSpacing.md)) {
            weekdayLabels.forEach { label ->
                Text(
                    text = label,
                    style = ElcheTheme.typography.label,
                    color = ElcheColor.InkMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f),
                )
            }
        }

        val leadingBlanks = (month.firstDay.dayOfWeek.isoDayNumber - 1).coerceIn(0, 6)
        val cells: List<Int?> = List(leadingBlanks) { null } + (1..month.daysInMonth).toList()
        cells.chunked(7).forEach { week ->
            Row(modifier = Modifier.fillMaxWidth()) {
                week.forEach { day ->
                    Box(
                        modifier = Modifier.weight(1f).heightIn(min = 56.dp).padding(2.dp),
                        contentAlignment = Alignment.TopCenter,
                    ) {
                        if (day != null) {
                            DayCell(day = day, match = matchesByDay[day])
                        }
                    }
                }
                repeat(7 - week.size) { Box(modifier = Modifier.weight(1f).heightIn(min = 56.dp)) }
            }
        }
    }
}

private fun dayOfMonthOf(match: Match): Int = match.kickoffInstant.toLocalDateTime(TimeZone.currentSystemDefault()).day

@Composable
private fun DayCell(
    day: Int,
    match: Match?,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = day.toString(), style = ElcheTheme.typography.bodyS)
        if (match != null) {
            val isHome = match.home.id == Team.ELCHE_ID
            val rival = if (isHome) match.away else match.home
            Text(text = rival.shortName, style = ElcheTheme.typography.label, maxLines = 1)
            Box(
                modifier =
                    Modifier
                        .background(
                            color = if (isHome) ElcheColor.CrestBlue else ElcheColor.CrestRed,
                            shape = RoundedCornerShape(4.dp),
                        ).padding(horizontal = 4.dp),
            ) {
                Text(
                    text = if (isHome) "CASA" else "FUERA",
                    style = ElcheTheme.typography.label,
                    color = ElcheColor.White,
                )
            }
        }
    }
}
