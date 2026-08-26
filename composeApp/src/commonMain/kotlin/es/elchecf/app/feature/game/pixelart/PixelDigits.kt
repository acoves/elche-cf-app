package es.elchecf.app.feature.game.pixelart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

private val DIGIT_ROWS: Map<Char, List<String>> =
    mapOf(
        '0' to listOf("01110", "10001", "10011", "10101", "11001", "10001", "01110"),
        '1' to listOf("00100", "01100", "00100", "00100", "00100", "00100", "01110"),
        '2' to listOf("01110", "10001", "00001", "00010", "00100", "01000", "11111"),
        '3' to listOf("11111", "00010", "00100", "00010", "00001", "10001", "01110"),
        '4' to listOf("00010", "00110", "01010", "10010", "11111", "00010", "00010"),
        '5' to listOf("11111", "10000", "11110", "00001", "00001", "10001", "01110"),
        '6' to listOf("00110", "01000", "10000", "11110", "10001", "10001", "01110"),
        '7' to listOf("11111", "00001", "00010", "00100", "01000", "01000", "01000"),
        '8' to listOf("01110", "10001", "10001", "01110", "10001", "10001", "01110"),
        '9' to listOf("01110", "10001", "10001", "01111", "00001", "00010", "01100"),
    )

/** Fuente pixelada 5x7 (estilo marcador LED) para el número de puntos del minijuego de Datigol. */
fun pixelDigitSprite(
    digit: Char,
    color: Color,
): PixelSprite {
    val rows = DIGIT_ROWS[digit] ?: DIGIT_ROWS.getValue('0')
    return PixelSprite(rows = rows.map { it.replace('1', 'x') }, palette = mapOf('x' to color))
}

fun DrawScope.drawPixelScore(
    text: String,
    topLeft: Offset,
    pixelSize: Float,
    color: Color,
    spacingPx: Float = pixelSize,
) {
    var cursorX = topLeft.x
    for (char in text) {
        val sprite = pixelDigitSprite(char, color)
        drawPixelSprite(sprite, Offset(cursorX, topLeft.y), pixelSize)
        cursorX += sprite.widthPx * pixelSize + spacingPx
    }
}
