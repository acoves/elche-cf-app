package es.elchecf.app.data.repository

import es.elchecf.app.data.PlayerDataSource
import es.elchecf.app.domain.model.Player
import es.elchecf.app.domain.repository.PlayerRepository

class PlayerRepositoryImpl(
    private val dataSource: PlayerDataSource,
) : PlayerRepository {
    override suspend fun getSquad(): List<Player> = dataSource.fetchSquad()
}
