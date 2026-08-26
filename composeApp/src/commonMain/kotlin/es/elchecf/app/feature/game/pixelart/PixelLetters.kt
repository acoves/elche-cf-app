package es.elchecf.app.feature.game.pixelart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

/** Solo las letras que hacen falta para el cartel "ELCHE" que aparece de fondo en el minijuego
 * de Datigol (Perfil → Extra) — no es un alfabeto completo, no hace falta para nada más. */
private val LETTER_ROWS: Map<Char, List<String>> =
    mapOf(
        'E' to listOf("11111", "10000", "11110", "10000", "10000", "10000", "11111"),
        'L' to listOf("10000", "10000", "10000", "10000", "10000", "10000", "11111"),
        'C' to listOf("01111", "10000", "10000", "10000", "10000", "10000", "01111"),
        'H' to listOf("10001", "10001", "10001", "11111", "10001", "10001", "10001"),
        ' ' to listOf("00000", "00000", "00000", "00000", "00000", "00000", "00000"),
    )

fun pixelLetterSprite(
    letter: Char,
    color: Color,
): PixelSprite {
    val rows = LETTER_ROWS[letter.uppercaseChar()] ?: LETTER_ROWS.getValue(' ')
    return PixelSprite(rows = rows.map { it.replace('1', 'x') }, palette = mapOf('x' to color))
}

fun DrawScope.drawPixelText(
    text: String,
    topLeft: Offset,
    pixelSize: Float,
    color: Color,
    spacingPx: Float = pixelSize,
) {
    var cursorX = topLeft.x
    for (char in text) {
        val sprite = pixelLetterSprite(char, color)
        drawPixelSprite(sprite, Offset(cursorX, topLeft.y), pixelSize)
        cursorX += sprite.widthPx * pixelSize + spacingPx
    }
}
