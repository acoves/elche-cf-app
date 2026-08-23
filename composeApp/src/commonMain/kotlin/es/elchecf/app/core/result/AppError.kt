package es.elchecf.app.core.result

sealed interface AppError {
    data object Unknown : AppError

    /** FASE 8: fallo de red real (sin conexión, timeout, HTTP no-2xx…) al llamar a football-data.org. */
    data class Network(
        val message: String,
    ) : AppError
}
