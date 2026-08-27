package es.elchecf.app.feature.home.favoriteplayers

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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

/**
 * Elegir capitán entre los jugadores favoritos ya seleccionados — mismo flujo que "Select your
 * captain" de la referencia, estilo oscuro con verde Elche.
 */
@Composable
fun FavoriteCaptainSelectScreen(
    favorites: List<SquadPlayer>,
    initialCaptain: Int?,
    onBack: () -> Unit,
    onConfirm: (Int) -> Unit,
) {
    var captain by remember { mutableStateOf(initialCaptain) }

    Column(modifier = Modifier.fillMaxSize().background(ElcheColor.Ink)) {
        FavoriteFlowHeader(title = "Elige tu capitán", onBack = onBack)
        Text(
            text = "Elige un capitán para liderar tu experiencia personalizada.",
            style = ElcheTheme.typography.bodyS,
            color = ElcheColor.White.copy(alpha = 0.7f),
            modifier = Modifier.padding(horizontal = ElcheSpacing.screenMargin),
        )
        LazyColumn(
            modifier = Modifier.weight(1f).padding(horizontal = ElcheSpacing.screenMargin),
            contentPadding = PaddingValues(vertical = ElcheSpacing.lg),
        ) {
            items(favorites) { player ->
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = ElcheSpacing.sm)
                            .clip(ElcheShape.Card)
                            .background(ElcheColor.White.copy(alpha = 0.06f))
                            .clickable(onClick = { captain = player.number })
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
                    CaptainMarker(selected = captain == player.number)
                }
            }
        }
        ElcheButton(
            text = "Confirmar capitán",
            onClick = { captain?.let(onConfirm) },
            enabled = captain != null,
            variant = ElcheButtonVariant.Accent,
            modifier = Modifier.fillMaxWidth().padding(ElcheSpacing.screenMargin),
        )
    }
}

@Composable
private fun CaptainMarker(selected: Boolean) {
    Box(
        modifier =
            Modifier
                .size(28.dp)
                .clip(CircleShape)
                .then(
                    if (selected) {
                        Modifier.background(ElcheColor.Green)
                    } else {
                        Modifier.border(1.5.dp, ElcheColor.White.copy(alpha = 0.4f), CircleShape)
                    },
                ),
        contentAlignment = Alignment.Center,
    ) {
        if (selected) {
            Text(text = "C", style = ElcheTheme.typography.label, color = ElcheColor.White)
        }
    }
}
