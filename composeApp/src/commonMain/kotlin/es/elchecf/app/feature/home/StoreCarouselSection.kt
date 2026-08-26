package es.elchecf.app.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import elcheapp.composeapp.generated.resources.Res
import elcheapp.composeapp.generated.resources.parati_elche_city
import elcheapp.composeapp.generated.resources.parati_kit_away
import elcheapp.composeapp.generated.resources.parati_kit_home
import elcheapp.composeapp.generated.resources.parati_kit_third
import elcheapp.composeapp.generated.resources.playfair_display_variable
import es.elchecf.app.designsystem.icon.ElcheProfileIcon
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheShape
import es.elchecf.app.designsystem.theme.ElcheSpacing
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource

/** Solo para el titular "Store" de esta sección — no es una tipografía de marca (esas son
 * Barlow Condensed/Inter, ver `designsystem/theme/ElcheFontFamily.kt`), el usuario pidió
 * Playfair Display concretamente aquí. */
@Composable
private fun playfairDisplayFontFamily(): FontFamily =
    FontFamily(
        Font(
            Res.font.playfair_display_variable,
            weight = FontWeight.Medium,
            variationSettings = FontVariation.Settings(FontVariation.weight(500)),
        ),
    )

private val storeCarouselImages: List<DrawableResource> =
    listOf(
        Res.drawable.parati_kit_home,
        Res.drawable.parati_kit_away,
        Res.drawable.parati_kit_third,
        Res.drawable.parati_elche_city,
    )

/**
 * Carrusel de la tienda al final de "Para ti" (fotos reales de campaña, `composeResources/
 * drawable/parati_*.jpg`): solo se ven las imágenes, sin texto encima ni al pulsarlas — solo el
 * titular "Store" + flecha llevan a Tienda. Referencia visual: sección "Chelsea FC Store" de la
 * app del Chelsea (imágenes casi a pantalla completa, se ve el filo de la siguiente).
 */
@Composable
fun StoreCarouselSection(
    onStoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(
            modifier =
                Modifier
                    .padding(horizontal = ElcheSpacing.screenMargin)
                    .clickable(onClick = onStoreClick),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Store",
                style =
                    TextStyle(
                        fontFamily = playfairDisplayFontFamily(),
                        fontWeight = FontWeight.Medium,
                        fontSize = 28.sp,
                        lineHeight = 32.sp,
                    ),
                color = ElcheColor.Ink,
                modifier = Modifier.weight(1f),
            )
            Icon(imageVector = ElcheProfileIcon.ArrowForward, contentDescription = null, tint = ElcheColor.Ink)
        }

        LazyRow(
            contentPadding = PaddingValues(start = ElcheSpacing.screenMargin, end = ElcheSpacing.xxxl),
            horizontalArrangement = Arrangement.spacedBy(ElcheSpacing.md),
            modifier = Modifier.padding(top = ElcheSpacing.md),
        ) {
            items(storeCarouselImages) { image ->
                Image(
                    painter = painterResource(image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier =
                        Modifier
                            .fillParentMaxWidth(CAROUSEL_ITEM_WIDTH_FRACTION)
                            .aspectRatio(CAROUSEL_ITEM_ASPECT_RATIO)
                            .clip(ElcheShape.CardLarge),
                )
            }
        }
    }
}

private const val CAROUSEL_ITEM_WIDTH_FRACTION = 0.85f
private const val CAROUSEL_ITEM_ASPECT_RATIO = 3f / 4f
