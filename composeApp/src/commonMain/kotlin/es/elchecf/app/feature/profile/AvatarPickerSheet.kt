package es.elchecf.app.feature.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import es.elchecf.app.designsystem.component.ElcheButton
import es.elchecf.app.designsystem.component.ElcheSheetHeader
import es.elchecf.app.designsystem.icon.ElcheProfileIcon
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheShape
import es.elchecf.app.designsystem.theme.ElcheSpacing

/**
 * Opciones de avatar (CLAUDE.md §10: nada de fotografía oficial del club sin permiso). Escudo real
 * del Elche vía football-data.org (mismo que usa el resto de la app) + fotos de Wikimedia Commons
 * con licencia libre, del estadio y de la ciudad de Elche.
 */
private val avatarOptions =
    listOf(
        "https://crests.football-data.org/285.png",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/c/ce/" +
            "Estadio_Mart%C3%ADnez_Valero.JPG/500px-Estadio_Mart%C3%ADnez_Valero.JPG",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/b/bc/" +
            "Palmeral_de_Elche_14.JPG/500px-Palmeral_de_Elche_14.JPG",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/9/9f/" +
            "Escudo_de_Elche_antiguo.JPG/500px-Escudo_de_Elche_antiguo.JPG",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7f/" +
            "Adidas_soccer_ball_on_a_grass_pitch_%28Unsplash%29.jpg/" +
            "500px-Adidas_soccer_ball_on_a_grass_pitch_%28Unsplash%29.jpg",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d1/" +
            "Soccer_match.jpg/500px-Soccer_match.jpg",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/1/16/" +
            "Spanish_football_fans.JPG/500px-Spanish_football_fans.JPG",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/d/dd/" +
            "Football_kit_01.jpg/500px-Football_kit_01.jpg",
        "https://upload.wikimedia.org/wikipedia/commons/c/c9/Elx_panoramica.jpg",
    )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvatarPickerSheet(
    currentAvatarUrl: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
) {
    var selected by remember { mutableStateOf(currentAvatarUrl) }
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = ElcheSpacing.lg)) {
            ElcheSheetHeader(
                title = "Elige un avatar",
                onClose = onDismiss,
                modifier = Modifier.padding(top = ElcheSpacing.md),
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(ElcheSpacing.sm),
                verticalArrangement = Arrangement.spacedBy(ElcheSpacing.sm),
                contentPadding = PaddingValues(vertical = ElcheSpacing.md),
                modifier = Modifier.height(GRID_HEIGHT.dp),
            ) {
                items(avatarOptions) { url ->
                    AvatarOption(url = url, isSelected = url == selected, onClick = { selected = url })
                }
            }
            ElcheButton(
                text = "Guardar cambios",
                onClick = { onSave(selected) },
                enabled = selected != currentAvatarUrl,
                modifier = Modifier.fillMaxWidth().padding(top = ElcheSpacing.md, bottom = ElcheSpacing.lg),
            )
        }
    }
}

@Composable
private fun AvatarOption(
    url: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .aspectRatio(1f)
                .clip(ElcheShape.Card)
                .background(ElcheColor.GreenSoft)
                .then(
                    if (isSelected) {
                        Modifier.border(BorderStroke(3.dp, ElcheColor.Green), ElcheShape.Card)
                    } else {
                        Modifier
                    },
                ).clickable(onClick = onClick),
    ) {
        AsyncImage(
            model = url,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().aspectRatio(1f),
        )
        if (isSelected) {
            Box(
                modifier =
                    Modifier
                        .padding(ElcheSpacing.xs)
                        .size(22.dp)
                        .background(ElcheColor.Green, CircleShape)
                        .align(Alignment.TopEnd),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = ElcheProfileIcon.Check,
                    contentDescription = "Seleccionado",
                    tint = ElcheColor.White,
                    modifier = Modifier.size(14.dp),
                )
            }
        }
    }
}

private const val GRID_HEIGHT = 420
