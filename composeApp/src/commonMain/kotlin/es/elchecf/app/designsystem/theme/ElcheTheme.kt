package es.elchecf.app.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf

private val LocalElcheTypography =
    compositionLocalOf<ElcheTypography> {
        error("No hay ElcheTypography en el árbol de composición: envuelve la pantalla en ElcheTheme.")
    }

/** Punto de acceso a los tokens de marca, análogo a `MaterialTheme` (CLAUDE.md §3). */
object ElcheTheme {
    val typography: ElcheTypography
        @Composable get() = LocalElcheTypography.current
}

@Composable
fun ElcheTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val elcheTypography = elcheTypography()
    val colorScheme = if (darkTheme) elcheDarkColorScheme() else elcheLightColorScheme()

    CompositionLocalProvider(LocalElcheTypography provides elcheTypography) {
        MaterialTheme(
            colorScheme = colorScheme,
            shapes = elcheShapes(),
            typography = elcheTypography.toMaterialTypography(),
            content = content,
        )
    }
}

// Mapeo a los slots de Material3 para que los componentes estándar (Button, TopAppBar…)
// hereden la tipografía de marca sin que cada pantalla tenga que aplicarla a mano.
private fun ElcheTypography.toMaterialTypography(): Typography =
    Typography(
        displayLarge = displayL,
        displayMedium = displayM,
        displaySmall = titleL,
        headlineLarge = titleL,
        headlineMedium = titleM,
        headlineSmall = titleM,
        titleLarge = titleL,
        titleMedium = titleM,
        titleSmall = body,
        bodyLarge = body,
        bodyMedium = bodyS,
        bodySmall = bodyS,
        labelLarge = label,
        labelMedium = label,
        labelSmall = label,
    )
