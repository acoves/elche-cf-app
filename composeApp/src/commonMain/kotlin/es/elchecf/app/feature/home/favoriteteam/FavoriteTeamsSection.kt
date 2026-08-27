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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import elcheapp.composeapp.generated.resources.Res
import elcheapp.composeapp.generated.resources.favteam_femenino
import elcheapp.composeapp.generated.resources.favteam_ilicitano
import elcheapp.composeapp.generated.resources.favteam_primer
import elcheapp.composeapp.generated.resources.playfair_display_variable
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheShape
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme
import es.elchecf.app.domain.model.ClubTeam
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource

/** Solo para el titular de esta sección — mismo criterio que Store/Game Zone, no es tipografía
 * de marca (ver `designsystem/theme/ElcheFontFamily.kt`). */
@Composable
private fun favTeamPlayfairFontFamily(): FontFamily =
    FontFamily(
        Font(
            Res.font.playfair_display_variable,
            weight = FontWeight.Medium,
            variationSettings = FontVariation.Settings(FontVariation.weight(500)),
        ),
    )

private fun ClubTeam.bannerRes(): DrawableResource =
    when (this) {
        ClubTeam.PrimerEquipo -> Res.drawable.favteam_primer
        ClubTeam.Femenino -> Res.drawable.favteam_femenino
        ClubTeam.Ilicitano -> Res.drawable.favteam_ilicitano
    }

/**
 * "Avisos de tu equipo" al final de Para ti (justo después de Jugadores favoritos): elige de
 * cuáles de los 3 equipos del Elche CF (primer equipo, femenino, ilicitano) quieres recibir un
 * aviso cuando jueguen. Fotos reales, mismo criterio que Store/Game Zone. Mejora post-Fase 7:
 * sin backend de notificaciones todavía (igual que Perfil → Notificaciones) — el estado del
 * interruptor es local, no dispara ningún aviso real todavía.
 */
@Composable
fun FavoriteTeamsSection(
    selectedTeams: Set<ClubTeam>,
    onToggle: (ClubTeam) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = "Avisos de tu equipo",
            style =
                TextStyle(
                    fontFamily = favTeamPlayfairFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 26.sp,
                    lineHeight = 30.sp,
                ),
            color = ElcheColor.Ink,
        )
        Text(
            text = "Elige los equipos del Elche CF de los que quieres recibir un aviso cuando jueguen.",
            style = ElcheTheme.typography.bodyS,
            color = ElcheColor.InkMuted,
            modifier = Modifier.padding(top = ElcheSpacing.xs),
        )
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = ElcheSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(ElcheSpacing.md),
        ) {
            ClubTeam.entries.forEach { team ->
                TeamBannerCard(
                    team = team,
                    selected = team in selectedTeams,
                    onClick = { onToggle(team) },
                )
            }
        }
    }
}

@Composable
private fun TeamBannerCard(
    team: ClubTeam,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier =
            Modifier
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
                Icon(imageVector = Icons.Filled.Check, contentDescription = "Avisos activados", tint = ElcheColor.White)
            }
        }
    }
}
