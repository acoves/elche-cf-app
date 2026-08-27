package es.elchecf.app.feature.home.favoriteplayers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.elchecf.app.designsystem.component.ElcheButton
import es.elchecf.app.designsystem.component.ElcheCard
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme

/**
 * "Jugadores favoritos" en Para ti, debajo de la tienda: mismo formato que la tarjeta de
 * favoritos de la referencia (foto de perfil grande del capitán + acciones, lista del resto de
 * favoritos debajo), en verde/dorado Elche. "Ver perfil" no lleva a ningún sitio a propósito —
 * no hay pantalla de ficha de jugador en la app — mientras que "Comprar camiseta" sí es real y
 * lleva a la tienda.
 */
@Composable
fun FavoritePlayersCard(
    favorites: List<SquadPlayer>,
    captain: SquadPlayer?,
    onEditClick: () -> Unit,
    onBuyShirtClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ElcheCard(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Jugadores favoritos",
                style =
                    TextStyle(
                        fontFamily = favoritePlayfairFontFamily(),
                        fontWeight = FontWeight.Medium,
                        fontSize = 24.sp,
                        lineHeight = 28.sp,
                    ),
                color = ElcheColor.Ink,
                modifier = Modifier.weight(1f),
            )
            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Editar jugadores favoritos",
                    tint = ElcheColor.Ink,
                )
            }
        }

        if (captain == null) {
            Text(
                text = "Elige tus jugadores favoritos para personalizar tu experiencia en la app.",
                style = ElcheTheme.typography.bodyS,
                color = ElcheColor.InkMuted,
                modifier = Modifier.padding(top = ElcheSpacing.sm),
            )
            FavoriteOutlinedButton(
                text = "Elegir jugadores favoritos",
                onClick = onEditClick,
                modifier = Modifier.fillMaxWidth().padding(top = ElcheSpacing.lg),
            )
            return@ElcheCard
        }

        Column(
            modifier = Modifier.fillMaxWidth().padding(top = ElcheSpacing.lg),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box {
                FavoritePlayerAvatar(size = 88.dp, ringColor = ElcheColor.Green)
                Box(
                    modifier =
                        Modifier
                            .align(Alignment.TopEnd)
                            .clip(CircleShape)
                            .background(ElcheColor.Gold)
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                ) {
                    Text(text = "C", style = ElcheTheme.typography.label, color = ElcheColor.GoldDeep)
                }
            }
            Text(
                text = captain.name,
                style = ElcheTheme.typography.titleM,
                modifier = Modifier.padding(top = ElcheSpacing.md),
            )
            Text(
                text = "Dorsal ${captain.number} · ${captain.position.label}",
                style = ElcheTheme.typography.bodyS,
                color = ElcheColor.InkMuted,
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = ElcheSpacing.md),
                horizontalArrangement = Arrangement.spacedBy(ElcheSpacing.sm),
            ) {
                // A propósito sin función: no hay pantalla de ficha de jugador en la app.
                FavoriteOutlinedButton(text = "Ver perfil", onClick = {}, modifier = Modifier.weight(1f))
                ElcheButton(text = "Comprar camiseta", onClick = onBuyShirtClick, modifier = Modifier.weight(1f))
            }
        }

        val otherFavorites = favorites.filter { it.number != captain.number }
        if (otherFavorites.isNotEmpty()) {
            HorizontalDivider(modifier = Modifier.padding(vertical = ElcheSpacing.lg), color = ElcheColor.Divider)
            Column(verticalArrangement = Arrangement.spacedBy(ElcheSpacing.md)) {
                otherFavorites.forEach { player ->
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        FavoritePlayerAvatar(size = 40.dp)
                        Column(modifier = Modifier.weight(1f).padding(start = ElcheSpacing.md)) {
                            Text(text = player.name, style = ElcheTheme.typography.body)
                            Text(
                                text = "Dorsal ${player.number}",
                                style = ElcheTheme.typography.label,
                                color = ElcheColor.InkMuted,
                            )
                        }
                        FavoriteOutlinedButton(text = "Comprar camiseta", onClick = onBuyShirtClick)
                    }
                }
            }
        }
    }
}
