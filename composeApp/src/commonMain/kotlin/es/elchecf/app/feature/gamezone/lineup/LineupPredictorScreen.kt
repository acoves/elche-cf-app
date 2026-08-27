package es.elchecf.app.feature.gamezone.lineup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import es.elchecf.app.core.util.CountdownParts
import es.elchecf.app.core.util.countdownFlow
import es.elchecf.app.core.util.toKickoffLabel
import es.elchecf.app.designsystem.component.ElcheButton
import es.elchecf.app.designsystem.component.ElcheButtonVariant
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheShape
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme
import es.elchecf.app.domain.model.Match
import es.elchecf.app.feature.home.favoriteplayers.FavoriteFlowHeader
import es.elchecf.app.feature.home.favoriteplayers.SquadPlayer
import kotlinx.coroutines.flow.collectLatest

/**
 * "Predice el once" (Game Zone): el usuario arma su propio once inicial con la formación que
 * quiera antes de que el club anuncie la alineación real, con cuenta atrás hasta el saque inicial
 * del [match] real (mismo dato que usa "Para ti") — cambia solo con leer ese partido, sin nada
 * hardcodeado. Sin comparación contra la alineación oficial real: no hay fuente de datos para
 * eso, así que se queda en predicción/personalización, igual que el resto de tarjetas de "Para
 * ti" (Predictor de resultado, Quiz).
 */
@Composable
fun LineupPredictorScreen(
    match: Match?,
    onBack: () -> Unit,
) {
    var formation by remember { mutableStateOf(lineupFormations.first()) }
    var assignments by remember { mutableStateOf<Map<Int, SquadPlayer>>(emptyMap()) }
    var activeSlot by remember { mutableStateOf<Int?>(null) }
    var showFormationPicker by remember { mutableStateOf(false) }
    var confirmed by remember { mutableStateOf(false) }

    val activeSlotPosition = activeSlot?.let { formation.slots.getOrNull(it)?.position }

    if (showFormationPicker) {
        FormationPickerScreen(
            selected = formation,
            onSelect = { picked ->
                formation = picked
                assignments = emptyMap()
                confirmed = false
                showFormationPicker = false
            },
            onBack = { showFormationPicker = false },
        )
        return
    }
    if (activeSlotPosition != null) {
        LineupPlayerPickerScreen(
            position = activeSlotPosition,
            usedNumbers = assignments.values.map { it.number }.toSet(),
            onSelect = { player ->
                assignments = assignments + (activeSlot!! to player)
                activeSlot = null
            },
            onBack = { activeSlot = null },
        )
        return
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(ElcheColor.GreenDeep, ElcheColor.Ink))),
    ) {
        FavoriteFlowHeader(title = "Predice el once", onBack = onBack)

        if (match != null) {
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = ElcheSpacing.screenMargin)) {
                Text(
                    text = "${match.home.shortName} vs ${match.away.shortName}",
                    style = ElcheTheme.typography.titleM,
                    color = ElcheColor.White,
                )
                Text(
                    text = match.kickoffInstant.toKickoffLabel(),
                    style = ElcheTheme.typography.bodyS,
                    color = ElcheColor.White.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = ElcheSpacing.xs),
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(ElcheSpacing.screenMargin),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier =
                    Modifier
                        .clip(ElcheShape.Pill)
                        .background(ElcheColor.Green.copy(alpha = 0.3f))
                        .clickable(onClick = { showFormationPicker = true })
                        .padding(horizontal = ElcheSpacing.md, vertical = ElcheSpacing.sm),
            ) {
                Text(
                    text = "Formación: ${formation.label}",
                    style = ElcheTheme.typography.label,
                    color = ElcheColor.White,
                )
            }
            IconButton(onClick = {
                assignments = emptyMap()
                confirmed = false
            }) {
                Icon(imageVector = Icons.Filled.Refresh, contentDescription = "Reiniciar once", tint = ElcheColor.White)
            }
        }

        LineupPitch(
            formation = formation,
            assignments = assignments,
            onSlotClick = { if (!confirmed) activeSlot = it },
            onSlotClear = { index -> if (!confirmed) assignments = assignments - index },
            modifier = Modifier.weight(1f).fillMaxWidth().padding(bottom = ElcheSpacing.lg),
        )

        LineupConfirmBar(
            filledCount = assignments.size,
            totalSlots = formation.slots.size,
            confirmed = confirmed,
            onConfirm = { confirmed = true },
            onEdit = { confirmed = false },
            match = match,
        )
    }
}

@Composable
private fun LineupConfirmBar(
    filledCount: Int,
    totalSlots: Int,
    confirmed: Boolean,
    onConfirm: () -> Unit,
    onEdit: () -> Unit,
    match: Match?,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(ElcheColor.Ink)
                .padding(ElcheSpacing.screenMargin),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (confirmed) {
            ElcheButton(
                text = "Once guardado ✓",
                onClick = {},
                enabled = false,
                variant = ElcheButtonVariant.Accent,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = "Editar once",
                style = ElcheTheme.typography.bodyS,
                color = ElcheColor.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = ElcheSpacing.sm).clickable(onClick = onEdit),
            )
        } else {
            ElcheButton(
                text =
                    if (filledCount ==
                        totalSlots
                    ) {
                        "Confirmar once"
                    } else {
                        "Elige a los $totalSlots ($filledCount/$totalSlots)"
                    },
                onClick = onConfirm,
                enabled = filledCount == totalSlots,
                variant = ElcheButtonVariant.Accent,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        if (match != null) {
            Text(
                text = "Se conocerá la alineación real en:",
                style = ElcheTheme.typography.label,
                color = ElcheColor.White.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = ElcheSpacing.md),
            )
            KickoffCountdownRow(match = match, modifier = Modifier.padding(top = ElcheSpacing.xs))
        }
    }
}

@Composable
private fun KickoffCountdownRow(
    match: Match,
    modifier: Modifier = Modifier,
) {
    var countdown by remember { mutableStateOf(CountdownParts(0, 0, 0, 0, isPast = false)) }
    LaunchedEffect(match.kickoffInstant) {
        countdownFlow(match.kickoffInstant).collectLatest { countdown = it }
    }
    if (countdown.isPast) {
        Text(
            text = "¡Ya se conoce la alineación!",
            style = ElcheTheme.typography.bodyS,
            color = ElcheColor.Gold,
            modifier = modifier,
        )
        return
    }
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(ElcheSpacing.sm)) {
        CountdownDigit(countdown.days, "DÍAS")
        CountdownDigit(countdown.hours, "H")
        CountdownDigit(countdown.minutes, "MIN")
        CountdownDigit(countdown.seconds, "S")
    }
}

@Composable
private fun CountdownDigit(
    value: Long,
    label: String,
) {
    Column(
        modifier =
            Modifier
                .width(
                    48.dp,
                ).clip(ElcheShape.Card)
                .background(ElcheColor.White.copy(alpha = 0.08f))
                .padding(vertical = ElcheSpacing.xs),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = value.toString().padStart(2, '0'),
            style = ElcheTheme.typography.monoNum,
            color = ElcheColor.Gold,
            textAlign = TextAlign.Center,
        )
        Text(text = label, style = ElcheTheme.typography.label, color = ElcheColor.White, textAlign = TextAlign.Center)
    }
}
