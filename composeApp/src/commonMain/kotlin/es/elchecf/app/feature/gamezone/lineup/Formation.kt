package es.elchecf.app.feature.gamezone.lineup

import es.elchecf.app.feature.home.favoriteplayers.PlayerPosition

/**
 * Posición de un hueco en el campo, en fracción de ancho/alto (0f..1f). [y] = 0 es la portería
 * propia (arriba en pantalla) y 1 el área rival (abajo) — el equipo ataca hacia abajo, igual que
 * en la referencia del usuario.
 */
data class FormationSlot(
    val position: PlayerPosition,
    val x: Float,
    val y: Float,
)

data class Formation(
    val label: String,
    val slots: List<FormationSlot>,
)

private fun gk() = FormationSlot(PlayerPosition.Goalkeeper, 0.5f, 0.08f)

/**
 * Las 8 formaciones que pidió el usuario (misma referencia visual): coordenadas trazadas a mano
 * para que se vean como un dibujo táctico real, no una rejilla genérica.
 */
val lineupFormations: List<Formation> =
    listOf(
        Formation(
            "4-4-2",
            listOf(
                gk(),
                FormationSlot(PlayerPosition.Defender, 0.12f, 0.28f),
                FormationSlot(PlayerPosition.Defender, 0.37f, 0.25f),
                FormationSlot(PlayerPosition.Defender, 0.63f, 0.25f),
                FormationSlot(PlayerPosition.Defender, 0.88f, 0.28f),
                FormationSlot(PlayerPosition.Midfielder, 0.12f, 0.55f),
                FormationSlot(PlayerPosition.Midfielder, 0.37f, 0.52f),
                FormationSlot(PlayerPosition.Midfielder, 0.63f, 0.52f),
                FormationSlot(PlayerPosition.Midfielder, 0.88f, 0.55f),
                FormationSlot(PlayerPosition.Forward, 0.35f, 0.82f),
                FormationSlot(PlayerPosition.Forward, 0.65f, 0.82f),
            ),
        ),
        Formation(
            "4-3-3",
            listOf(
                gk(),
                FormationSlot(PlayerPosition.Defender, 0.12f, 0.28f),
                FormationSlot(PlayerPosition.Defender, 0.37f, 0.25f),
                FormationSlot(PlayerPosition.Defender, 0.63f, 0.25f),
                FormationSlot(PlayerPosition.Defender, 0.88f, 0.28f),
                FormationSlot(PlayerPosition.Midfielder, 0.25f, 0.52f),
                FormationSlot(PlayerPosition.Midfielder, 0.50f, 0.55f),
                FormationSlot(PlayerPosition.Midfielder, 0.75f, 0.52f),
                FormationSlot(PlayerPosition.Forward, 0.15f, 0.82f),
                FormationSlot(PlayerPosition.Forward, 0.50f, 0.86f),
                FormationSlot(PlayerPosition.Forward, 0.85f, 0.82f),
            ),
        ),
        Formation(
            "3-4-3",
            listOf(
                gk(),
                FormationSlot(PlayerPosition.Defender, 0.22f, 0.26f),
                FormationSlot(PlayerPosition.Defender, 0.50f, 0.23f),
                FormationSlot(PlayerPosition.Defender, 0.78f, 0.26f),
                FormationSlot(PlayerPosition.Midfielder, 0.10f, 0.53f),
                FormationSlot(PlayerPosition.Midfielder, 0.37f, 0.50f),
                FormationSlot(PlayerPosition.Midfielder, 0.63f, 0.50f),
                FormationSlot(PlayerPosition.Midfielder, 0.90f, 0.53f),
                FormationSlot(PlayerPosition.Forward, 0.15f, 0.82f),
                FormationSlot(PlayerPosition.Forward, 0.50f, 0.86f),
                FormationSlot(PlayerPosition.Forward, 0.85f, 0.82f),
            ),
        ),
        Formation(
            "5-3-2",
            listOf(
                gk(),
                FormationSlot(PlayerPosition.Defender, 0.06f, 0.28f),
                FormationSlot(PlayerPosition.Defender, 0.28f, 0.25f),
                FormationSlot(PlayerPosition.Defender, 0.50f, 0.23f),
                FormationSlot(PlayerPosition.Defender, 0.72f, 0.25f),
                FormationSlot(PlayerPosition.Defender, 0.94f, 0.28f),
                FormationSlot(PlayerPosition.Midfielder, 0.25f, 0.55f),
                FormationSlot(PlayerPosition.Midfielder, 0.50f, 0.58f),
                FormationSlot(PlayerPosition.Midfielder, 0.75f, 0.55f),
                FormationSlot(PlayerPosition.Forward, 0.35f, 0.84f),
                FormationSlot(PlayerPosition.Forward, 0.65f, 0.84f),
            ),
        ),
        Formation(
            "5-4-1",
            listOf(
                gk(),
                FormationSlot(PlayerPosition.Defender, 0.06f, 0.28f),
                FormationSlot(PlayerPosition.Defender, 0.28f, 0.25f),
                FormationSlot(PlayerPosition.Defender, 0.50f, 0.23f),
                FormationSlot(PlayerPosition.Defender, 0.72f, 0.25f),
                FormationSlot(PlayerPosition.Defender, 0.94f, 0.28f),
                FormationSlot(PlayerPosition.Midfielder, 0.12f, 0.55f),
                FormationSlot(PlayerPosition.Midfielder, 0.37f, 0.52f),
                FormationSlot(PlayerPosition.Midfielder, 0.63f, 0.52f),
                FormationSlot(PlayerPosition.Midfielder, 0.88f, 0.55f),
                FormationSlot(PlayerPosition.Forward, 0.50f, 0.86f),
            ),
        ),
        Formation(
            "4-2-3-1",
            listOf(
                gk(),
                FormationSlot(PlayerPosition.Defender, 0.12f, 0.28f),
                FormationSlot(PlayerPosition.Defender, 0.37f, 0.25f),
                FormationSlot(PlayerPosition.Defender, 0.63f, 0.25f),
                FormationSlot(PlayerPosition.Defender, 0.88f, 0.28f),
                FormationSlot(PlayerPosition.Midfielder, 0.35f, 0.46f),
                FormationSlot(PlayerPosition.Midfielder, 0.65f, 0.46f),
                FormationSlot(PlayerPosition.Midfielder, 0.18f, 0.68f),
                FormationSlot(PlayerPosition.Midfielder, 0.50f, 0.64f),
                FormationSlot(PlayerPosition.Midfielder, 0.82f, 0.68f),
                FormationSlot(PlayerPosition.Forward, 0.50f, 0.88f),
            ),
        ),
        Formation(
            "3-4-2-1",
            listOf(
                gk(),
                FormationSlot(PlayerPosition.Defender, 0.22f, 0.26f),
                FormationSlot(PlayerPosition.Defender, 0.50f, 0.23f),
                FormationSlot(PlayerPosition.Defender, 0.78f, 0.26f),
                FormationSlot(PlayerPosition.Midfielder, 0.10f, 0.50f),
                FormationSlot(PlayerPosition.Midfielder, 0.37f, 0.47f),
                FormationSlot(PlayerPosition.Midfielder, 0.63f, 0.47f),
                FormationSlot(PlayerPosition.Midfielder, 0.90f, 0.50f),
                FormationSlot(PlayerPosition.Midfielder, 0.35f, 0.70f),
                FormationSlot(PlayerPosition.Midfielder, 0.65f, 0.70f),
                FormationSlot(PlayerPosition.Forward, 0.50f, 0.89f),
            ),
        ),
        Formation(
            "3-5-2",
            listOf(
                gk(),
                FormationSlot(PlayerPosition.Defender, 0.22f, 0.26f),
                FormationSlot(PlayerPosition.Defender, 0.50f, 0.23f),
                FormationSlot(PlayerPosition.Defender, 0.78f, 0.26f),
                FormationSlot(PlayerPosition.Midfielder, 0.06f, 0.55f),
                FormationSlot(PlayerPosition.Midfielder, 0.28f, 0.51f),
                FormationSlot(PlayerPosition.Midfielder, 0.50f, 0.54f),
                FormationSlot(PlayerPosition.Midfielder, 0.72f, 0.51f),
                FormationSlot(PlayerPosition.Midfielder, 0.94f, 0.55f),
                FormationSlot(PlayerPosition.Forward, 0.35f, 0.84f),
                FormationSlot(PlayerPosition.Forward, 0.65f, 0.84f),
            ),
        ),
    )
