package es.elchecf.app.feature.game.datigol

import androidx.compose.ui.graphics.Color
import es.elchecf.app.feature.game.pixelart.PixelSprite

/**
 * Datigol, mascota actual del Elche CF: zorro naranja con una cresta verde tipo mohawk solo en
 * lo alto de la cabeza, ojos verdes, camiseta blanca con una franja verde horizontal en el
 * pecho, pantalón blanco y botas negras (interpretación en bits a partir de fotos reales que el
 * usuario compartió — no hay asset oficial, todo se define como texturas de caracteres, ver
 * [PixelSprite], para poder ajustarlo a mano sin depender de ningún archivo de imagen).
 */
private val FOX_ORANGE = Color(0xFFE8792A)
private val CREST_GREEN = Color(0xFF05642C)
private val KIT_WHITE = Color(0xFFFFFFFF)
private val OUTLINE_BLACK = Color(0xFF201B14)

private val DATIGOL_PALETTE =
    mapOf(
        'o' to FOX_ORANGE,
        'g' to CREST_GREEN,
        'w' to KIT_WHITE,
        'k' to OUTLINE_BLACK,
    )

private fun row(vararg runs: Pair<Char, Int>): String =
    buildString { runs.forEach { (char, count) -> repeat(count) { append(char) } } }

// Cabeza: común a las tres poses (de pie, saltando, corriendo) — solo cambian brazos/piernas
// por debajo de la fila 8. La cresta verde (filas 2-4) es solo mohawk, no baja hasta los ojos.
private val HEAD =
    listOf(
        row('.' to 3, 'o' to 1, '.' to 8, 'o' to 1, '.' to 5),
        row('.' to 2, 'o' to 3, '.' to 6, 'o' to 3, '.' to 4),
        row('.' to 1, 'o' to 4, '.' to 2, 'g' to 2, '.' to 2, 'o' to 4, '.' to 3),
        row('.' to 1, 'o' to 5, 'g' to 4, 'o' to 5, '.' to 3),
        row('.' to 1, 'o' to 6, 'g' to 2, 'o' to 6, '.' to 3),
        row('.' to 1, 'o' to 2, 'k' to 1, 'o' to 8, 'k' to 1, 'o' to 2, '.' to 3),
        row('.' to 2, 'o' to 4, 'w' to 4, 'o' to 4, '.' to 4),
        row('.' to 2, 'o' to 4, 'w' to 1, 'k' to 2, 'w' to 1, 'o' to 4, '.' to 4),
        row('.' to 3, 'o' to 10, '.' to 5),
    )

// Hombros/camiseta: comunes a de pie y corriendo (en el salto los brazos se separan del cuerpo).
private val SHOULDERS_STANDING =
    listOf(
        row('.' to 1, 'o' to 2, 'w' to 10, 'o' to 2, '.' to 3),
        row('.' to 1, 'o' to 2, 'g' to 10, 'o' to 2, '.' to 3),
        row('.' to 1, 'o' to 2, 'w' to 10, 'o' to 3, '.' to 2),
    )

private val SHORTS =
    row('.' to 1, 'o' to 2, 'w' to 8, 'o' to 4, 'w' to 1, '.' to 2)

/** Quieto, de pie sobre una plataforma. */
fun datigolIdleSprite(): PixelSprite =
    PixelSprite(
        rows =
            HEAD + SHOULDERS_STANDING +
                listOf(
                    SHORTS,
                    row('.' to 1, 'o' to 2, 'w' to 8, 'o' to 3, 'w' to 2, '.' to 2),
                    row('.' to 1, 'o' to 3, '.' to 6, 'o' to 5, 'w' to 1, '.' to 2),
                    row('.' to 1, 'o' to 3, '.' to 6, 'o' to 3, '.' to 5),
                    row('.' to 1, 'o' to 3, '.' to 6, 'o' to 3, '.' to 5),
                    row('.' to 1, 'k' to 3, '.' to 6, 'k' to 3, '.' to 5),
                    row('.' to 1, 'k' to 3, '.' to 6, 'k' to 3, '.' to 5),
                ),
        palette = DATIGOL_PALETTE,
    )

/** En el aire: brazos arriba y separados, piernas encogidas — la pose que se ve casi todo el
 * rato al saltar. */
fun datigolJumpSprite(): PixelSprite =
    PixelSprite(
        rows =
            HEAD +
                listOf(
                    row('o' to 3, 'w' to 9, 'o' to 3, '.' to 3),
                    row('o' to 2, '.' to 1, 'g' to 10, '.' to 1, 'o' to 2, '.' to 2),
                    row('.' to 1, 'o' to 2, 'w' to 10, 'o' to 3, '.' to 2),
                    SHORTS,
                    row('.' to 6, 'o' to 6, '.' to 6),
                    row('.' to 6, 'o' to 4, 'k' to 2, '.' to 6),
                    row('.' to 18),
                    row('.' to 18),
                    row('.' to 18),
                    row('.' to 18),
                ),
        palette = DATIGOL_PALETTE,
    )

/** Corriendo hacia la derecha (zancada) — para la izquierda se usa `flipX` al dibujar, no hace
 * falta otro sprite. */
fun datigolRunSprite(): PixelSprite =
    PixelSprite(
        rows =
            HEAD + SHOULDERS_STANDING +
                listOf(
                    SHORTS,
                    row('.' to 2, 'o' to 3, '.' to 7, 'o' to 3, '.' to 3),
                    row('.' to 2, 'k' to 3, '.' to 4, 'o' to 3, '.' to 6),
                    row('.' to 9, 'k' to 3, '.' to 6),
                    row('.' to 18),
                    row('.' to 18),
                    row('.' to 18),
                ),
        palette = DATIGOL_PALETTE,
    )

private val PLATFORM_PALETTE = mapOf('g' to CREST_GREEN, 'w' to KIT_WHITE)

/** Plataforma: franja verde/blanco/verde, referencia directa al "franjiverde". */
fun platformSprite(): PixelSprite =
    PixelSprite(
        rows = listOf(row('g' to 10), row('w' to 10), row('g' to 10)),
        palette = PLATFORM_PALETTE,
    )

private val BALL_PALETTE = mapOf('w' to KIT_WHITE, 'k' to OUTLINE_BLACK)

/** Balón pixelado — elemento decorativo del fondo con parallax. */
fun ballSprite(): PixelSprite =
    PixelSprite(
        rows =
            listOf(
                row('.' to 2, 'w' to 4, '.' to 2),
                row('.' to 1, 'w' to 6, '.' to 1),
                row('w' to 2, 'k' to 1, 'w' to 2, 'k' to 1, 'w' to 2),
                row('w' to 8),
                row('w' to 8),
                row('w' to 2, 'k' to 1, 'w' to 2, 'k' to 1, 'w' to 2),
                row('.' to 1, 'w' to 6, '.' to 1),
                row('.' to 2, 'w' to 4, '.' to 2),
            ),
        palette = BALL_PALETTE,
    )
