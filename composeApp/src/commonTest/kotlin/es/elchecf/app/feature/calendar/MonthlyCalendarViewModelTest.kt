package es.elchecf.app.feature.calendar

import app.cash.turbine.test
import es.elchecf.app.core.result.AppError
import es.elchecf.app.core.result.AppResult
import es.elchecf.app.domain.model.ClubTeam
import es.elchecf.app.domain.model.Match
import es.elchecf.app.domain.model.MatchStatus
import es.elchecf.app.domain.model.Prediction
import es.elchecf.app.domain.model.Team
import es.elchecf.app.domain.repository.MatchRepository
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
import kotlin.time.Clock

private val elche =
    Team(id = Team.ELCHE_ID, name = "Elche CF", shortName = "ELX", crestUrl = "", primaryColorHex = "#00A650")
private val rival = Team(id = "1", name = "Rival CF", shortName = "RIV", crestUrl = "", primaryColorHex = "#FFFFFF")

private val sampleMatch =
    Match(
        id = "m1",
        home = elche,
        away = rival,
        kickoffInstant = Clock.System.now(),
        competition = "LaLiga Hypermotion",
        venue = "Martínez Valero",
        status = MatchStatus.Scheduled,
    )

private class FakeMatchRepository(
    private val seasonMatchesResult: AppResult<List<Match>, AppError> = AppResult.Success(emptyList()),
) : MatchRepository {
    val calls = mutableListOf<ClubTeam>()

    override suspend fun getNextMatch(): AppResult<Match?, AppError> = AppResult.Success(null)

    override suspend fun getSeasonMatches(team: ClubTeam): AppResult<List<Match>, AppError> {
        calls.add(team)
        return seasonMatchesResult
    }

    override suspend fun submitPrediction(prediction: Prediction): AppResult<Unit, AppError> = AppResult.Success(Unit)
}

@OptIn(ExperimentalCoroutinesApi::class)
class MonthlyCalendarViewModelTest {
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `carga el calendario del primer equipo al iniciar`() =
        runTest {
            val viewModel = MonthlyCalendarViewModel(FakeMatchRepository(AppResult.Success(listOf(sampleMatch))))

            viewModel.uiState.test {
                val state = awaitItem()
                assertEquals(ClubTeam.PrimerEquipo, state.selectedTeam)
                assertEquals(listOf(sampleMatch), state.matches)
                assertFalse(state.isLoading)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `un fallo al cargar el calendario establece un mensaje de error`() =
        runTest {
            val viewModel = MonthlyCalendarViewModel(FakeMatchRepository(AppResult.Failure(AppError.Unknown)))

            viewModel.uiState.test {
                val state = awaitItem()
                assertFalse(state.isLoading)
                assertTrue(state.matches.isEmpty())
                assertEquals("No se pudo cargar el calendario.", state.error)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `selectTeam con un equipo distinto recarga el calendario`() =
        runTest {
            val repository = FakeMatchRepository()
            val viewModel = MonthlyCalendarViewModel(repository)

            viewModel.selectTeam(ClubTeam.Ilicitano)

            assertEquals(ClubTeam.Ilicitano, viewModel.uiState.value.selectedTeam)
            assertEquals(listOf(ClubTeam.PrimerEquipo, ClubTeam.Ilicitano), repository.calls)
        }

    @Test
    fun `selectTeam con el mismo equipo no recarga`() =
        runTest {
            val repository = FakeMatchRepository()
            val viewModel = MonthlyCalendarViewModel(repository)

            viewModel.selectTeam(ClubTeam.PrimerEquipo)

            assertEquals(listOf(ClubTeam.PrimerEquipo), repository.calls)
        }

    @Test
    fun `goToNextMonth y goToPreviousMonth cambian el mes mostrado`() =
        runTest {
            val viewModel = MonthlyCalendarViewModel(FakeMatchRepository())
            val initialMonth = viewModel.uiState.value.displayedMonth

            viewModel.goToNextMonth()
            assertEquals(initialMonth.next(), viewModel.uiState.value.displayedMonth)

            viewModel.goToPreviousMonth()
            assertEquals(initialMonth, viewModel.uiState.value.displayedMonth)
        }
}
