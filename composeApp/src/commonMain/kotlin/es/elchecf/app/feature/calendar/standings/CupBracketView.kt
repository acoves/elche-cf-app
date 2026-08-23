package es.elchecf.app.feature.calendar.standings

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheShape
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme
import es.elchecf.app.domain.model.CupRound
import es.elchecf.app.domain.model.CupTie
import es.elchecf.app.domain.model.Team

private val TIE_HEIGHT = 64.dp
private val TIE_WIDTH = 168.dp
private val ROUND0_UNIT = 88.dp

/**
 * Bracket octavos → final (CLAUDE.md §5.2). Scroll horizontal (rondas) + vertical (eliminatorias).
 * FASE 5: la alineación vertical entre rondas ya sitúa cada cruce sobre sus dos predecesores; las
 * líneas conectoras dibujadas (Canvas) quedan como pulido visual pendiente, no bloquean la lectura
 * del bracket (los cuadros ya quedan alineados con sus padres).
 */
@Composable
fun CupBracketView(ties: List<CupTie>) {
    val rounds =
        listOf(
            CupRound.RoundOf16 to "OCTAVOS",
            CupRound.QuarterFinal to "CUARTOS",
            CupRound.SemiFinal to "SEMIS",
            CupRound.Final to "FINAL",
        )
    Row(modifier = Modifier.horizontalScroll(rememberScrollState()).padding(ElcheSpacing.lg)) {
        rounds.forEachIndexed { roundIndex, (round, label) ->
            val roundTies = ties.filter { it.round == round }
            val topPadding = ROUND0_UNIT * ((1 shl roundIndex) - 1) / 2
            val gapBetween = ROUND0_UNIT * (1 shl roundIndex) - TIE_HEIGHT

            Column(modifier = Modifier.width(TIE_WIDTH + ElcheSpacing.md).verticalScroll(rememberScrollState())) {
                Text(text = label, style = ElcheTheme.typography.label, color = ElcheColor.InkMuted)
                Spacer(modifier = Modifier.height(ElcheSpacing.sm + topPadding))
                roundTies.forEachIndexed { tieIndex, tie ->
                    CupTieCard(tie)
                    if (tieIndex != roundTies.lastIndex) {
                        Spacer(modifier = Modifier.height(gapBetween))
                    }
                }
            }
        }
    }
}

@Composable
private fun CupTieCard(tie: CupTie) {
    Column(
        modifier =
            Modifier
                .width(TIE_WIDTH)
                .height(TIE_HEIGHT)
                .clip(ElcheShape.Card)
                .background(ElcheColor.White),
    ) {
        CupTieTeamRow(
            team = tie.home,
            goals = tie.aggregate?.homeGoals,
            isWinner = tie.winner?.id == tie.home.id,
            modifier = Modifier.weight(1f),
        )
        CupTieTeamRow(
            team = tie.away,
            goals = tie.aggregate?.awayGoals,
            isWinner = tie.winner?.id == tie.away.id,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun CupTieTeamRow(
    team: Team,
    goals: Int?,
    isWinner: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .background(if (isWinner) ElcheColor.Ink else ElcheColor.GreenSoft)
                .padding(horizontal = ElcheSpacing.sm),
    ) {
        Text(
            text = team.shortName,
            style = ElcheTheme.typography.bodyS,
            color = if (isWinner) ElcheColor.White else ElcheColor.Ink,
            fontWeight = if (isWinner) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = goals?.toString() ?: "–",
            style = ElcheTheme.typography.bodyS,
            color = if (isWinner) ElcheColor.White else ElcheColor.Ink,
            fontWeight = if (isWinner) FontWeight.Bold else FontWeight.Normal,
        )
    }
}
