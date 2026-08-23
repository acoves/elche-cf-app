package es.elchecf.app.data

import es.elchecf.app.core.result.AppError
import es.elchecf.app.core.result.AppResult
import kotlinx.coroutines.flow.StateFlow

interface AuthDataSource {
    val isLoggedIn: StateFlow<Boolean>

    suspend fun login(): AppResult<Unit, AppError>

    suspend fun logout()
}
