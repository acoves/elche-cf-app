package es.elchecf.app.feature.calendar.standings

import app.cash.turbine.test
import es.elchecf.app.core.result.AppError
import es.elchecf.app.core.result.AppResult
import es.elchecf.app.domain.model.ClubTeam
import es.elchecf.app.domain.model.CupRound
import es.elchecf.app.domain.model.CupTie
import es.elchecf.app.domain.model.StandingRow
import es.elchecf.app.domain.model.Team
import es.elchecf.app.domain.repository.Competition
import es.elchecf.app.domain.repository.CupRepository
import es.elchecf.app.domain.repository.StandingsRepository
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

private val elche =
    Team(id = Team.ELCHE_ID, name = "Elche CF", shortName = "ELX", crestUrl = "", primaryColorHex = "#00A650")
private val rival = Team(id = "1", name = "Rival CF", shortName = "RIV", crestUrl = "", primaryColorHex = "#FFFFFF")

private class FakeStandingsRepository(
    private val result: AppResult<List<StandingRow>, AppError> = AppResult.Success(emptyList()),
) : StandingsRepository {
    val calls = mutableListOf<Pair<Competition, ClubTeam>>()

    override suspend fun getStandings(
        competition: Competition,
        team: ClubTeam,
    ): AppResult<List<StandingRow>, AppError> {
        calls.add(competition to team)
        return result
    }
}

private class FakeCupRepository(
    private val bracket: List<CupTie> = emptyList(),
) : CupRepository {
    override suspend fun getBracket(): List<CupTie> = bracket
}

@OptIn(ExperimentalCoroutinesApi::class)
class StandingsViewModelTest {
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `carga clasificacion de LaLiga y bracket de copa al iniciar`() =
        runTest {
            val row =
                StandingRow(
                    position = 1,
                    team = elche,
                    played = 5,
                    won = 3,
                    drawn = 1,
                    lost = 1,
                    goalDiff = 4,
                    points = 10,
                )
            val tie = CupTie(round = CupRound.RoundOf16, home = elche, away = rival, aggregate = null, winner = null)
            val viewModel =
                StandingsViewModel(
                    FakeStandingsRepository(AppResult.Success(listOf(row))),
                    FakeCupRepository(listOf(tie)),
                )

            viewModel.uiState.test {
                val state = awaitItem()
                assertEquals(listOf(row), state.standings)
                assertEquals(listOf(tie), state.cupBracket)
                assertFalse(state.isLoading)
                assertEquals(Competition.LaLiga, state.selectedCompetition)
                assertEquals(ClubTeam.PrimerEquipo, state.selectedTeam)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `un fallo al cargar la clasificacion establece un mensaje de error`() =
        runTest {
            val viewModel =
                StandingsViewModel(FakeStandingsRepository(AppResult.Failure(AppError.Unknown)), FakeCupRepository())

            viewModel.uiState.test {
                val state = awaitItem()
                assertFalse(state.isLoading)
                assertTrue(state.standings.isEmpty())
                assertEquals("No se pudo cargar la clasificación.", state.error)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `selectCompetition a Copa no vuelve a pedir la clasificacion de LaLiga`() =
        runTest {
            val row =
                StandingRow(
                    position = 1,
                    team = elche,
                    played = 5,
                    won = 3,
                    drawn = 1,
                    lost = 1,
                    goalDiff = 4,
                    points = 10,
                )
            val repository = FakeStandingsRepository(AppResult.Success(listOf(row)))
            val viewModel = StandingsViewModel(repository, FakeCupRepository())

            viewModel.selectCompetition(Competition.Copa)

            assertEquals(Competition.Copa, viewModel.uiState.value.selectedCompetition)
            assertEquals(1, repository.calls.size)
        }

    @Test
    fun `selectTeam con un equipo distinto dispara una nueva carga`() =
        runTest {
            val repository = FakeStandingsRepository()
            val viewModel = StandingsViewModel(repository, FakeCupRepository())

            viewModel.selectTeam(ClubTeam.Femenino)

            assertEquals(ClubTeam.Femenino, viewModel.uiState.value.selectedTeam)
            assertEquals(
                listOf(Competition.LaLiga to ClubTeam.PrimerEquipo, Competition.LaLiga to ClubTeam.Femenino),
                repository.calls,
            )
        }

    @Test
    fun `selectTeam con el mismo equipo no recarga`() =
        runTest {
            val repository = FakeStandingsRepository()
            val viewModel = StandingsViewModel(repository, FakeCupRepository())

            viewModel.selectTeam(ClubTeam.PrimerEquipo)

            assertEquals(1, repository.calls.size)
        }
}
