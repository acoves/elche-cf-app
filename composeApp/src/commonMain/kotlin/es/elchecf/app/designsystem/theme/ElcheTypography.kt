package es.elchecf.app.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Escala tipográfica de marca (CLAUDE.md §4.2). [displayL], [displayM], [titleL] y [titleM] usan
 * el registro condensado y deben pasarse en MAYÚSCULAS por quien los consume — [TextStyle] no
 * aplica transformación de mayúsculas, hay que llamar a `.uppercase()` sobre el texto.
 */
data class ElcheTypography(
    val displayL: TextStyle,
    val displayM: TextStyle,
    val titleL: TextStyle,
    val titleM: TextStyle,
    val body: TextStyle,
    val bodyS: TextStyle,
    val label: TextStyle,
    /** Números de cuenta atrás/marcador: tabular-nums para que no "salten" al cambiar de dígito. */
    val monoNum: TextStyle,
)

@Composable
internal fun elcheTypography(): ElcheTypography {
    val display = barlowCondensedFontFamily()
    val body = interFontFamily()

    return ElcheTypography(
        displayL =
            TextStyle(
                fontFamily = display,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                lineHeight = 44.sp,
                letterSpacing = (-0.3).sp,
            ),
        displayM =
            TextStyle(
                fontFamily = display,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                lineHeight = 36.sp,
                letterSpacing = (-0.3).sp,
            ),
        titleL =
            TextStyle(
                fontFamily = display,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                lineHeight = 28.sp,
                letterSpacing = (-0.2).sp,
            ),
        titleM =
            TextStyle(
                fontFamily = display,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                lineHeight = 24.sp,
                letterSpacing = (-0.2).sp,
            ),
        body =
            TextStyle(
                fontFamily = body,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 22.sp,
            ),
        bodyS =
            TextStyle(
                fontFamily = body,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            ),
        label =
            TextStyle(
                fontFamily = body,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                lineHeight = 16.sp,
            ),
        monoNum =
            TextStyle(
                fontFamily = body,
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
                lineHeight = 32.sp,
                fontFeatureSettings = "tnum",
            ),
    )
}
