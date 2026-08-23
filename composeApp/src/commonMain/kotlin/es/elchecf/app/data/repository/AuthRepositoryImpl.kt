package es.elchecf.app.data.repository

import es.elchecf.app.core.result.AppError
import es.elchecf.app.core.result.AppResult
import es.elchecf.app.data.AuthDataSource
import es.elchecf.app.domain.repository.AuthRepository
import kotlinx.coroutines.flow.StateFlow

class AuthRepositoryImpl(
    private val dataSource: AuthDataSource,
) : AuthRepository {
    override val isLoggedIn: StateFlow<Boolean> = dataSource.isLoggedIn

    override suspend fun login(): AppResult<Unit, AppError> = dataSource.login()

    override suspend fun logout() = dataSource.logout()
}
