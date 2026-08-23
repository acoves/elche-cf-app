package es.elchecf.app.data.mock

import es.elchecf.app.core.result.AppError
import es.elchecf.app.core.result.AppResult
import es.elchecf.app.data.AuthDataSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/** FASE 7: sesión en memoria, empieza como socio ya conectado — no hay pantalla de credenciales todavía. */
class MockAuthDataSource : AuthDataSource {
    private val _isLoggedIn = MutableStateFlow(true)
    override val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    override suspend fun login(): AppResult<Unit, AppError> {
        delay(NETWORK_DELAY_MS)
        _isLoggedIn.value = true
        return AppResult.Success(Unit)
    }

    override suspend fun logout() {
        delay(NETWORK_DELAY_MS)
        _isLoggedIn.value = false
    }

    private companion object {
        const val NETWORK_DELAY_MS = 300L
    }
}
