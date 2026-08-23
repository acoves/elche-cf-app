package es.elchecf.app

import androidx.compose.ui.window.ComposeUIViewController
import es.elchecf.app.di.initKoin
import platform.UIKit.UIViewController

// FASE 1 (iOS): pendiente de verificar en Mac. Código escrito siguiendo el patrón estándar de
// entry point de Compose Multiplatform para iOS; no se ha podido compilar ni ejecutar en
// Xcode/simulador por no disponer de macOS en este entorno de desarrollo.
private var koinInitialized = false

fun MainViewController(): UIViewController {
    if (!koinInitialized) {
        initKoin()
        koinInitialized = true
    }
    return ComposeUIViewController { App() }
}
