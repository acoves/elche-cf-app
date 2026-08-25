package es.elchecf.app.feature.home

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
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

private val elche =
    Team(id = Team.ELCHE_ID, name = "Elche CF", shortName = "ELX", crestUrl = "", primaryColorHex = "#00A650")
private val rival = Team(id = "1", name = "Rival CF", shortName = "RIV", crestUrl = "", primaryColorHex = "#FFFFFF")

private val scheduledMatch =
    Match(
        id = "m1",
        home = elche,
        away = rival,
        kickoffInstant = Clock.System.now() + 2.days,
        competition = "LaLiga Hypermotion",
        venue = "Martínez Valero",
        status = MatchStatus.Scheduled,
    )

private class FakeMatchRepository(
    private val nextMatchResult: AppResult<Match?, AppError> = AppResult.Success(null),
    private val predictionResult: AppResult<Unit, AppError> = AppResult.Success(Unit),
) : MatchRepository {
    var submitPredictionCalls = 0
    var lastPrediction: Prediction? = null

    override suspend fun getNextMatch(): AppResult<Match?, AppError> = nextMatchResult

    override suspend fun getSeasonMatches(team: ClubTeam): AppResult<List<Match>, AppError> =
        AppResult.Success(emptyList())

    override suspend fun submitPrediction(prediction: Prediction): AppResult<Unit, AppError> {
        submitPredictionCalls++
        lastPrediction = prediction
        return predictionResult
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class ForYouViewModelTest {
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `carga el proximo partido si la llamada tiene exito`() =
        runTest {
            val viewModel = ForYouViewModel(FakeMatchRepository(nextMatchResult = AppResult.Success(scheduledMatch)))

            viewModel.uiState.test {
                val state = awaitItem()
                assertEquals(scheduledMatch, state.match)
                assertFalse(state.isLoading)
                assertNull(state.error)
                assertEquals(3 to 5, state.quizScore)
                assertFalse(state.showValorateMatch)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `marca showValorateMatch cuando el partido acabo hace menos de 48 horas`() =
        runTest {
            val finishedMatch =
                scheduledMatch.copy(
                    status = MatchStatus.Finished,
                    kickoffInstant =
                        Clock.System.now() - 1.hours,
                )
            val viewModel = ForYouViewModel(FakeMatchRepository(nextMatchResult = AppResult.Success(finishedMatch)))

            viewModel.uiState.test {
                assertTrue(awaitItem().showValorateMatch)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `un fallo al cargar el partido establece un mensaje de error`() =
        runTest {
            val viewModel =
                ForYouViewModel(FakeMatchRepository(nextMatchResult = AppResult.Failure(AppError.Network("timeout"))))

            viewModel.uiState.test {
                val state = awaitItem()
                assertFalse(state.isLoading)
                assertNull(state.match)
                assertEquals("No se pudo cargar el próximo partido.", state.error)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `submitPrediction marca predictionSent cuando hay partido cargado`() =
        runTest {
            val repository = FakeMatchRepository(nextMatchResult = AppResult.Success(scheduledMatch))
            val viewModel = ForYouViewModel(repository)

            viewModel.submitPrediction(2, 1)

            assertEquals(1, repository.submitPredictionCalls)
            assertEquals(Prediction(scheduledMatch.id, 2, 1), repository.lastPrediction)
            assertTrue(viewModel.uiState.value.predictionSent)
        }

    @Test
    fun `submitPrediction no hace nada si todavia no hay partido cargado`() =
        runTest {
            val repository = FakeMatchRepository(nextMatchResult = AppResult.Success(null))
            val viewModel = ForYouViewModel(repository)

            viewModel.submitPrediction(2, 1)

            assertEquals(0, repository.submitPredictionCalls)
        }
}
