package es.elchecf.app.feature.gamezone.lineup

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheShape
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme
import es.elchecf.app.feature.home.favoriteplayers.FavoriteFlowHeader

/** Selector de formación: rejilla de mini-campos dibujados a partir de los mismos datos de
 * [lineupFormations] (no imágenes estáticas), para que el punteado sea exacto. */
@Composable
fun FormationPickerScreen(
    selected: Formation,
    onSelect: (Formation) -> Unit,
    onBack: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize().background(ElcheColor.Ink)) {
        FavoriteFlowHeader(title = "Elige la formación", onBack = onBack)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(ElcheSpacing.screenMargin),
            horizontalArrangement = Arrangement.spacedBy(ElcheSpacing.md),
            verticalArrangement = Arrangement.spacedBy(ElcheSpacing.md),
            modifier = Modifier.fillMaxSize(),
        ) {
            items(lineupFormations) { formation ->
                FormationCard(
                    formation = formation,
                    isSelected = formation.label == selected.label,
                    onClick = { onSelect(formation) },
                )
            }
        }
    }
}

@Composable
private fun FormationCard(
    formation: Formation,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(ElcheShape.Card)
                .background(if (isSelected) ElcheColor.Green.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.06f))
                .border(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) ElcheColor.Gold else Color.White.copy(alpha = 0.2f),
                    shape = ElcheShape.Card,
                ).clickable(onClick = onClick)
                .padding(ElcheSpacing.md),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(text = formation.label, style = ElcheTheme.typography.titleM, color = ElcheColor.White)
        }
        Canvas(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.8f)
                    .padding(top = ElcheSpacing.sm),
        ) {
            val dotRadius = size.minDimension * 0.045f
            formation.slots.forEach { slot ->
                drawCircle(
                    color = if (isSelected) ElcheColor.Gold else Color.White,
                    radius = dotRadius,
                    center = Offset(size.width * slot.x, size.height * slot.y),
                )
            }
        }
    }
}
