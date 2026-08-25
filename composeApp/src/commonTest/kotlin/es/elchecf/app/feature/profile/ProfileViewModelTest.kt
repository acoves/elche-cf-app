package es.elchecf.app.feature.profile

import app.cash.turbine.test
import es.elchecf.app.core.result.AppError
import es.elchecf.app.core.result.AppResult
import es.elchecf.app.domain.model.Benefit
import es.elchecf.app.domain.model.UserProfile
import es.elchecf.app.domain.repository.AuthRepository
import es.elchecf.app.domain.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private val defaultProfile =
    UserProfile(
        id = "1",
        firstName = "Antonio",
        lastName = "Franjiverde",
        avatarUrl = "",
        memberStatusLabel = "Socio · 2 años",
    )

private class FakeAuthRepository : AuthRepository {
    private val _isLoggedIn = MutableStateFlow(true)
    override val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    var loginCalls = 0
    var logoutCalls = 0

    override suspend fun login(): AppResult<Unit, AppError> {
        loginCalls++
        _isLoggedIn.value = true
        return AppResult.Success(Unit)
    }

    override suspend fun logout() {
        logoutCalls++
        _isLoggedIn.value = false
    }
}

private class FakeProfileRepository(
    initialProfile: UserProfile = defaultProfile,
    private val benefits: List<Benefit> = emptyList(),
) : ProfileRepository {
    private val _profile = MutableStateFlow(initialProfile)
    override val profile: StateFlow<UserProfile> = _profile

    var lastUpdatedNames: Pair<String, String>? = null
    var lastUpdatedAvatar: String? = null

    override suspend fun getBenefits(): List<Benefit> = benefits

    override fun updateProfile(
        firstName: String,
        lastName: String,
    ) {
        lastUpdatedNames = firstName to lastName
        _profile.value = _profile.value.copy(firstName = firstName, lastName = lastName)
    }

    override fun updateAvatar(avatarUrl: String) {
        lastUpdatedAvatar = avatarUrl
        _profile.value = _profile.value.copy(avatarUrl = avatarUrl)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `carga perfil y beneficios al iniciar`() =
        runTest {
            val benefit = Benefit(id = "1", title = "Descuento", subtitle = "10%", detail = "detalle", imageUrl = "")
            val viewModel =
                ProfileViewModel(
                    profileRepository = FakeProfileRepository(benefits = listOf(benefit)),
                    authRepository = FakeAuthRepository(),
                )

            viewModel.uiState.test {
                val state = awaitItem()
                assertEquals(defaultProfile, state.profile)
                assertEquals(listOf(benefit), state.benefits)
                assertTrue(state.isLoggedIn)
                assertTrue(!state.isLoading)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `login delega en AuthRepository`() =
        runTest {
            val authRepository = FakeAuthRepository()
            val viewModel = ProfileViewModel(FakeProfileRepository(), authRepository)

            viewModel.login()

            assertEquals(1, authRepository.loginCalls)
        }

    @Test
    fun `logout delega en AuthRepository`() =
        runTest {
            val authRepository = FakeAuthRepository()
            val viewModel = ProfileViewModel(FakeProfileRepository(), authRepository)

            viewModel.logout()

            assertEquals(1, authRepository.logoutCalls)
        }

    @Test
    fun `updateProfile actualiza el nombre en el repositorio`() =
        runTest {
            val profileRepository = FakeProfileRepository()
            val viewModel = ProfileViewModel(profileRepository, FakeAuthRepository())

            viewModel.updateProfile("Elena", "Verde")

            assertEquals("Elena" to "Verde", profileRepository.lastUpdatedNames)
        }

    @Test
    fun `updateAvatar actualiza la url en el repositorio`() =
        runTest {
            val profileRepository = FakeProfileRepository()
            val viewModel = ProfileViewModel(profileRepository, FakeAuthRepository())

            viewModel.updateAvatar("https://example.com/avatar.png")

            assertEquals("https://example.com/avatar.png", profileRepository.lastUpdatedAvatar)
        }
}
