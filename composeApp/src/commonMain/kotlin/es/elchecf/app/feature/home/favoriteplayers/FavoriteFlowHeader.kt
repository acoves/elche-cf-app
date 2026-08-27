package es.elchecf.app.feature.home.favoriteplayers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import elcheapp.composeapp.generated.resources.Res
import elcheapp.composeapp.generated.resources.playfair_display_variable
import es.elchecf.app.designsystem.icon.ElcheCalendarIcon
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheSpacing
import org.jetbrains.compose.resources.Font

/** Solo para los titulares de este flujo (referencia: tipografía editorial de la app original) —
 * no es tipografía de marca, ver el mismo patrón en `StoreCarouselSection.kt`/`GameZoneBanner.kt`. */
@Composable
internal fun favoritePlayfairFontFamily(): FontFamily =
    FontFamily(
        Font(
            Res.font.playfair_display_variable,
            weight = FontWeight.Medium,
            variationSettings = FontVariation.Settings(FontVariation.weight(500)),
        ),
    )

/** Cabecera oscura común a las pantallas de selección de jugadores/capitán favoritos. */
@Composable
internal fun FavoriteFlowHeader(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(ElcheSpacing.screenMargin),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = ElcheCalendarIcon.Previous,
            contentDescription = "Volver",
            tint = ElcheColor.White,
            modifier = Modifier.clickable(onClick = onBack),
        )
        Text(
            text = title,
            style =
                TextStyle(
                    fontFamily = favoritePlayfairFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 26.sp,
                    lineHeight = 30.sp,
                ),
            color = ElcheColor.White,
            modifier = Modifier.padding(start = ElcheSpacing.md),
        )
    }
}
