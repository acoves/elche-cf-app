package es.elchecf.app.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import elcheapp.composeapp.generated.resources.Res
import elcheapp.composeapp.generated.resources.gamezone_banner
import elcheapp.composeapp.generated.resources.playfair_display_variable
import es.elchecf.app.designsystem.component.ElcheButton
import es.elchecf.app.designsystem.theme.ElcheShape
import es.elchecf.app.designsystem.theme.ElcheSpacing
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource

@Composable
private fun playfairDisplayFontFamily(): FontFamily =
    FontFamily(
        Font(
            Res.font.playfair_display_variable,
            weight = FontWeight.Medium,
            variationSettings = FontVariation.Settings(FontVariation.weight(500)),
        ),
    )

/**
 * Banner de "Game Zone" en "Para ti" (foto real, `composeResources/drawable/gamezone_banner.jpg`,
 * jugadores del Elche celebrando): título "Game Zone" en Playfair Display integrado en la propia
 * imagen, con un botón debajo que lleva al hub de minijuegos (ver `feature/gamezone/
 * GameZoneScreen.kt`). Referencia visual: sección "Game Zone" de la app del Chelsea.
 */
@Composable
fun GameZoneBanner(
    onGameZoneClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .aspectRatio(4f / 5f)
                .clip(ElcheShape.CardLarge),
    ) {
        Image(
            painter = painterResource(Res.drawable.gamezone_banner),
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
                            0.6f to Color.Black.copy(alpha = 0.35f),
                            1f to Color.Black.copy(alpha = 0.8f),
                        ),
                    ),
        )
        Column(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(ElcheSpacing.lg)) {
            androidx.compose.material3.Text(
                text = "Game Zone",
                style =
                    TextStyle(
                        fontFamily = playfairDisplayFontFamily(),
                        fontWeight = FontWeight.Medium,
                        fontSize = 32.sp,
                        lineHeight = 36.sp,
                    ),
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = ElcheSpacing.md),
            )
            ElcheButton(
                text = "Game Zone →",
                onClick = onGameZoneClick,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
