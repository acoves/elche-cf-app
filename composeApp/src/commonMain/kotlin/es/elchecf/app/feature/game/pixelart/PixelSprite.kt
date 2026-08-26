package es.elchecf.app.feature.game.pixelart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

/**
 * Sprite "de bits": cada fila es una cadena de caracteres, cada carácter es una clave de
 * [palette] ('.' siempre transparente). Autoría a mano como arte ASCII — pensado para el
 * minijuego de Datigol (Perfil → Extra), no para nada más de la app.
 */
data class PixelSprite(
    val rows: List<String>,
    val palette: Map<Char, Color>,
) {
    val widthPx: Int = rows.maxOf { it.length }
    val heightPx: Int = rows.size
}

fun DrawScope.drawPixelSprite(
    sprite: PixelSprite,
    topLeft: Offset,
    pixelSize: Float,
    flipX: Boolean = false,
) {
    for (y in sprite.rows.indices) {
        val row = sprite.rows[y]
        for (x in row.indices) {
            val color = sprite.palette[row[x]] ?: continue
            val drawX = if (flipX) (sprite.widthPx - 1 - x) else x
            drawRect(
                color = color,
                topLeft = Offset(topLeft.x + drawX * pixelSize, topLeft.y + y * pixelSize),
                size = Size(pixelSize, pixelSize),
            )
        }
    }
}
