package es.elchecf.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme

/**
 * FASE 2: ElcheTheme (colores/tipografía/formas de marca) ya aplicado. El contenido de abajo
 * solo sirve para verificar visualmente displayL (condensada) y body (Inter) hasta la Fase 3.
 * FASE 3: se sustituye por RootNavHost + BottomBar.
 */
@Composable
fun App() {
    ElcheTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Box(modifier = Modifier.fillMaxSize().safeDrawingPadding().padding(ElcheSpacing.screenMargin)) {
                Column {
                    // FASE 1: placeholder; se retira en Fase 3
                    Text(text = "ELCHE CF".uppercase(), style = ElcheTheme.typography.displayL)
                    Text(text = "Fase 2: design system en construcción", style = ElcheTheme.typography.body)
                }
            }
        }
    }
}
