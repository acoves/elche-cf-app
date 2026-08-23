package es.elchecf.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * FASE 1: pantalla vacía de verificación, MaterialTheme por defecto, sin colores de marca.
 * FASE 2: se sustituye MaterialTheme por ElcheTheme.
 * FASE 3: el contenido fijo se sustituye por RootNavHost + BottomBar.
 */
@Composable
fun App() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Box(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
                Text(text = "Elche CF") // FASE 1: placeholder; se retira en Fase 3
            }
        }
    }
}
