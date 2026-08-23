package es.elchecf.app.core.result

/** Resultado tipado de operaciones que pueden fallar. Nunca se lanzan excepciones a la UI (CLAUDE.md §3). */
sealed interface AppResult<out T, out E> {
    data class Success<T>(
        val value: T,
    ) : AppResult<T, Nothing>

    data class Failure<E>(
        val error: E,
    ) : AppResult<Nothing, E>
}
