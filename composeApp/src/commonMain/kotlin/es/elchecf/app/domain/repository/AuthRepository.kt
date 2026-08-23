package es.elchecf.app.domain.repository

import es.elchecf.app.core.result.AppError
import es.elchecf.app.core.result.AppResult
import kotlinx.coroutines.flow.StateFlow

/** CLAUDE.md §5.5: "Auth mockeada en Fase 7; sin backend real hasta que se decida proveedor." */
interface AuthRepository {
    val isLoggedIn: StateFlow<Boolean>

    suspend fun login(): AppResult<Unit, AppError>

    suspend fun logout()
}
