package es.elchecf.app.core.result

/** FASE 4: solo lo que necesita el mock. FASE 8: se amplía con errores de red reales (HTTP, timeout…). */
sealed interface AppError {
    data object Unknown : AppError
}
