package es.elchecf.app.core.util

import androidx.compose.ui.graphics.Color

/** Convierte `Team.primaryColorHex` ("#05642C") en un [Color] de Compose. */
fun String.toColorOrNull(): Color? {
    val hex = removePrefix("#")
    if (hex.length != 6) return null
    val rgb = hex.toIntOrNull(radix = 16) ?: return null
    return Color(red = (rgb shr 16) and 0xFF, green = (rgb shr 8) and 0xFF, blue = rgb and 0xFF)
}
