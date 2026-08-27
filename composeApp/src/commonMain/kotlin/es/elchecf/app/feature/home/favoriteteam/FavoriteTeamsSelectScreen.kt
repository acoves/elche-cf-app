package es.elchecf.app.feature.home.favoriteteam

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import elcheapp.composeapp.generated.resources.Res
import elcheapp.composeapp.generated.resources.favteam_femenino
import elcheapp.composeapp.generated.resources.favteam_ilicitano
import elcheapp.composeapp.generated.resources.favteam_primer
import es.elchecf.app.designsystem.component.ElcheButton
import es.elchecf.app.designsystem.component.ElcheButtonVariant
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheShape
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme
import es.elchecf.app.domain.model.ClubTeam
import es.elchecf.app.feature.home.favoriteplayers.FavoriteFlowHeader
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

internal fun ClubTeam.bannerRes(): DrawableResource =
    when (this) {
        ClubTeam.PrimerEquipo -> Res.drawable.favteam_primer
        ClubTeam.Femenino -> Res.drawable.favteam_femenino
        ClubTeam.Ilicitano -> Res.drawable.favteam_ilicitano
    }

/**
 * Pantalla propia para elegir los equipos favoritos (no un modal encima de Para ti) — mismo
 * patrón de "tarjeta resumen + pantalla de selección aparte" que jugadores favoritos. Fotos
 * reales, estilo Elche verde/dorado sobre fondo oscuro, como la referencia de selección de
 * equipos del usuario.
 */
@Composable
fun FavoriteTeamsSelectScreen(
    initialSelection: Set<ClubTeam>,
    onBack: () -> Unit,
    onSave: (Set<ClubTeam>) -> Unit,
) {
    var selected by remember { mutableStateOf(initialSelection) }

    Column(modifier = Modifier.fillMaxSize().background(ElcheColor.Ink)) {
        FavoriteFlowHeader(title = "Equipos favoritos", onBack = onBack)
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = ElcheSpacing.screenMargin)) {
            Text(
                text = "Elige los equipos del Elche CF de los que quieres recibir un aviso cuando jueguen.",
                style = ElcheTheme.typography.bodyS,
                color = ElcheColor.White.copy(alpha = 0.7f),
            )
            Text(
                text = "Equipos seleccionados (${selected.size}/${ClubTeam.entries.size})",
                style = ElcheTheme.typography.titleM,
                color = ElcheColor.White,
                modifier = Modifier.padding(top = ElcheSpacing.lg),
            )
        }
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = ElcheSpacing.screenMargin, vertical = ElcheSpacing.md),
            verticalArrangement = Arrangement.spacedBy(ElcheSpacing.md),
        ) {
            ClubTeam.entries.forEach { team ->
                TeamBannerCard(
                    team = team,
                    selected = team in selected,
                    onClick = {
                        selected = if (team in selected) selected - team else selected + team
                    },
                )
            }
        }
        ElcheButton(
            text = "Guardar equipos",
            onClick = { onSave(selected) },
            variant = ElcheButtonVariant.Accent,
            modifier = Modifier.fillMaxWidth().padding(ElcheSpacing.screenMargin),
        )
    }
}

@Composable
internal fun TeamBannerCard(
    team: ClubTeam,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .aspectRatio(900f / 410f)
                .clip(ElcheShape.CardLarge)
                .clickable(onClick = onClick),
    ) {
        Image(
            painter = painterResource(team.bannerRes()),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            0f to Color.Transparent,
                            0.5f to Color.Black.copy(alpha = 0.25f),
                            1f to Color.Black.copy(alpha = 0.75f),
                        ),
                    ),
        )
        Text(
            text = team.label.uppercase(),
            style = ElcheTheme.typography.titleM,
            color = ElcheColor.White,
            modifier = Modifier.align(Alignment.BottomStart).padding(ElcheSpacing.lg),
        )
        Box(
            modifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(ElcheSpacing.md)
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(if (selected) ElcheColor.Green else Color.Black.copy(alpha = 0.35f))
                    .border(
                        width = 1.5.dp,
                        color = if (selected) Color.Transparent else Color.White.copy(alpha = 0.7f),
                        shape = CircleShape,
                    ),
            contentAlignment = Alignment.Center,
        ) {
            if (selected) {
                Icon(imageVector = Icons.Filled.Check, contentDescription = "Seleccionado", tint = ElcheColor.White)
            }
        }
    }
}
