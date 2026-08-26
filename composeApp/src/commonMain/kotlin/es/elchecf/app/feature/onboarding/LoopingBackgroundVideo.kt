package es.elchecf.app.feature.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Vídeo de fondo a pantalla completa, en bucle y sin sonido — pantalla de bienvenida sin sesión
 * (ver [OnboardingScreen]). Cada plataforma usa su propio reproductor nativo, así que es
 * `expect`/`actual` en vez de una librería multiplataforma de vídeo.
 */
@Composable
expect fun LoopingBackgroundVideo(modifier: Modifier = Modifier)
