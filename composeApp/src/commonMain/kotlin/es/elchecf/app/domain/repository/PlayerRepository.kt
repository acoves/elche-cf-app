package es.elchecf.app.domain.repository

import es.elchecf.app.domain.model.Player

interface PlayerRepository {
    suspend fun getSquad(): List<Player>
}
