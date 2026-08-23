package es.elchecf.app.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

// FASE 1: módulo vacío a propósito, nada que inyectar todavía.
// FASE 2+: se añaden módulos de designsystem/data/domain/viewmodel según se crean.
val appModule: Module = module {}

/**
 * Punto de entrada único de Koin, común a Android e iOS.
 * Android lo invoca desde ElcheApplication.onCreate() (módulo androidApp).
 * iOS lo invoca desde MainViewController antes de crear la vista de Compose.
 */
fun initKoin(config: KoinAppDeclaration = {}) {
    startKoin {
        config()
        modules(appModule, dataModule, viewModelModule)
    }
}
