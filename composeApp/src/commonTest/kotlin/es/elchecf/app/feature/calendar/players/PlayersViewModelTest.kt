package es.elchecf.app.feature.calendar.players

import app.cash.turbine.test
import es.elchecf.app.domain.model.Player
import es.elchecf.app.domain.model.PlayerPosition
import es.elchecf.app.domain.repository.PlayerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

private class FakePlayerRepository(
    private val squad: List<Player> = emptyList(),
) : PlayerRepository {
    override suspend fun getSquad(): List<Player> = squad
}

@OptIn(ExperimentalCoroutinesApi::class)
class PlayersViewModelTest {
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `carga la plantilla al iniciar`() =
        runTest {
            val player =
                Player(
                    id = "1",
                    firstName = "Iván",
                    lastName = "Marcone",
                    number = 6,
                    position = PlayerPosition.Midfielder,
                    photoUrl = "",
                )
            val viewModel = PlayersViewModel(FakePlayerRepository(squad = listOf(player)))

            viewModel.uiState.test {
                val state = awaitItem()
                assertFalse(state.isLoading)
                assertEquals(listOf(player), state.squad)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `si el repositorio no devuelve jugadores la plantilla queda vacia`() =
        runTest {
            val viewModel = PlayersViewModel(FakePlayerRepository(squad = emptyList()))

            viewModel.uiState.test {
                val state = awaitItem()
                assertFalse(state.isLoading)
                assertTrue(state.squad.isEmpty())
                cancelAndIgnoreRemainingEvents()
            }
        }
}
