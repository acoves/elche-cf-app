package es.elchecf.app.data.mock

import es.elchecf.app.data.PlayerDataSource
import es.elchecf.app.domain.model.Player
import es.elchecf.app.domain.model.PlayerPosition
import kotlinx.coroutines.delay

// FASE 5: plantilla de ejemplo, nombres genéricos (no la plantilla real del club — CLAUDE.md §10).
private val squad =
    listOf(
        Player("gk-1", "Iván", "Moreno", 1, PlayerPosition.Goalkeeper, ""),
        Player("gk-2", "Marc", "Iglesias", 13, PlayerPosition.Goalkeeper, ""),
        Player("gk-3", "Rubén", "Cano", 25, PlayerPosition.Goalkeeper, ""),
        Player("df-1", "Diego", "Herrero", 2, PlayerPosition.Defender, ""),
        Player("df-2", "Álvaro", "Muñoz", 3, PlayerPosition.Defender, ""),
        Player("df-3", "Sergio", "Vidal", 4, PlayerPosition.Defender, ""),
        Player("df-4", "Iker", "Blanco", 5, PlayerPosition.Defender, ""),
        Player("df-5", "Pau", "Serra", 15, PlayerPosition.Defender, ""),
        Player("df-6", "Hugo", "Campos", 16, PlayerPosition.Defender, ""),
        Player("df-7", "Nico", "Aguilar", 23, PlayerPosition.Defender, ""),
        Player("mf-1", "Mateo", "Prieto", 6, PlayerPosition.Midfielder, ""),
        Player("mf-2", "Adrián", "Vega", 8, PlayerPosition.Midfielder, ""),
        Player("mf-3", "Bruno", "Santana", 10, PlayerPosition.Midfielder, ""),
        Player("mf-4", "Gonzalo", "Rico", 14, PlayerPosition.Midfielder, ""),
        Player("mf-5", "Yeray", "Nuño", 18, PlayerPosition.Midfielder, ""),
        Player("mf-6", "Marc", "Falcón", 20, PlayerPosition.Midfielder, ""),
        Player("fw-1", "Lucas", "Solano", 7, PlayerPosition.Forward, ""),
        Player("fw-2", "Rodrigo", "Peña", 9, PlayerPosition.Forward, ""),
        Player("fw-3", "Ismael", "Bravo", 11, PlayerPosition.Forward, ""),
        Player("fw-4", "Kevin", "Osorio", 17, PlayerPosition.Forward, ""),
        Player("fw-5", "Toni", "Delgado", 19, PlayerPosition.Forward, ""),
    )

class MockPlayerDataSource : PlayerDataSource {
    override suspend fun fetchSquad(): List<Player> {
        delay(NETWORK_DELAY_MS)
        return squad
    }

    private companion object {
        const val NETWORK_DELAY_MS = 300L
    }
}
