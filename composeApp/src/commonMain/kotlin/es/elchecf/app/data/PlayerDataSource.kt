package es.elchecf.app.data

import es.elchecf.app.domain.model.Player

interface PlayerDataSource {
    suspend fun fetchSquad(): List<Player>
}
