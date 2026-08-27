package es.elchecf.app.feature.home.favoriteteam

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import es.elchecf.app.designsystem.component.ElcheCard
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme
import es.elchecf.app.domain.model.ClubTeam
import es.elchecf.app.feature.home.favoriteplayers.FavoriteOutlinedButton
import org.jetbrains.compose.resources.painterResource

/**
 * "Avisos de tu equipo" en Para ti (al final, justo después de Jugadores favoritos): resumen +
 * lápiz que abre [FavoriteTeamsSelectScreen] — misma estructura que Jugadores favoritos, no un
 * toggle directo aquí. "Ver equipo" lleva de verdad al Calendario del equipo elegido.
 */
@Composable
fun FavoriteTeamsCard(
    selectedTeams: List<ClubTeam>,
    onEditClick: () -> Unit,
    onViewTeamClick: (ClubTeam) -> Unit,
    modifier: Modifier = Modifier,
) {
    ElcheCard(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Avisos de tu equipo", style = ElcheTheme.typography.titleM, modifier = Modifier.weight(1f))
            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Editar equipos favoritos",
                    tint = ElcheColor.Ink,
                )
            }
        }

        if (selectedTeams.isEmpty()) {
            Text(
                text = "Elige los equipos del Elche CF de los que quieres recibir un aviso cuando jueguen.",
                style = ElcheTheme.typography.bodyS,
                color = ElcheColor.InkMuted,
                modifier = Modifier.padding(top = ElcheSpacing.sm),
            )
            FavoriteOutlinedButton(
                text = "Elegir equipos",
                onClick = onEditClick,
                modifier = Modifier.fillMaxWidth().padding(top = ElcheSpacing.lg),
            )
        } else {
            Column(
                modifier = Modifier.padding(top = ElcheSpacing.lg),
                verticalArrangement = Arrangement.spacedBy(ElcheSpacing.md),
            ) {
                selectedTeams.forEach { team ->
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(team.bannerRes()),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(48.dp).clip(CircleShape),
                        )
                        Text(
                            text = team.label,
                            style = ElcheTheme.typography.body,
                            modifier = Modifier.weight(1f).padding(start = ElcheSpacing.md),
                        )
                        FavoriteOutlinedButton(text = "Ver equipo", onClick = { onViewTeamClick(team) })
                    }
                }
            }
        }
    }
}
