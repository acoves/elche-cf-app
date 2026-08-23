package es.elchecf.app.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import elcheapp.composeapp.generated.resources.Res
import elcheapp.composeapp.generated.resources.barlow_condensed_bold
import elcheapp.composeapp.generated.resources.barlow_condensed_semibold
import elcheapp.composeapp.generated.resources.inter_variable
import org.jetbrains.compose.resources.Font

/**
 * Barlow Condensed (CLAUDE.md §4.2, alternativa a Archivo Condensed): registro de titulares,
 * solo se usa en 600/700. Ficheros estáticos, sin ejes de variación.
 */
@Composable
internal fun barlowCondensedFontFamily(): FontFamily =
    FontFamily(
        Font(Res.font.barlow_condensed_semibold, weight = FontWeight.SemiBold),
        Font(Res.font.barlow_condensed_bold, weight = FontWeight.Bold),
    )

/**
 * Inter (CLAUDE.md §4.2): cuerpo y datos, 400/500/600. Fichero variable único; cada peso se
 * obtiene con [FontVariation.Settings] sobre el eje `wght` (soportado en iOS/desktop desde CMP 1.8).
 */
@Composable
internal fun interFontFamily(): FontFamily =
    FontFamily(
        Font(
            Res.font.inter_variable,
            weight = FontWeight.Normal,
            variationSettings = FontVariation.Settings(FontVariation.weight(400)),
        ),
        Font(
            Res.font.inter_variable,
            weight = FontWeight.Medium,
            variationSettings = FontVariation.Settings(FontVariation.weight(500)),
        ),
        Font(
            Res.font.inter_variable,
            weight = FontWeight.SemiBold,
            variationSettings = FontVariation.Settings(FontVariation.weight(600)),
        ),
    )
