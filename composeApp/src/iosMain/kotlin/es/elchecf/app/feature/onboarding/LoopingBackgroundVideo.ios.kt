package es.elchecf.app.feature.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

// FASE 9 (iOS): pendiente de verificar en Mac — de momento un fondo negro en vez de AVPlayer en
// bucle, para no dejar la pantalla de bienvenida vacía si esto se llega a compilar para iOS.
@Composable
actual fun LoopingBackgroundVideo(modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize().background(Color.Black))
}
