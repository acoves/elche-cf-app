package es.elchecf.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import es.elchecf.app.designsystem.component.ElcheButton
import es.elchecf.app.designsystem.component.ElcheButtonVariant
import es.elchecf.app.designsystem.component.ElcheCard
import es.elchecf.app.designsystem.component.ElcheTopBar
import es.elchecf.app.designsystem.component.Franja
import es.elchecf.app.designsystem.component.SectionHeader
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme

/**
 * FASE 2: galería temporal de los componentes base del design system (botón, card, franja,
 * section header, top bar), solo para verificar visualmente que ElcheTheme se aplica bien.
 * FASE 3: se sustituye por RootNavHost + BottomBar.
 */
@Composable
fun App() {
    ElcheTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .safeDrawingPadding()
                        .verticalScroll(rememberScrollState()),
            ) {
                ElcheTopBar(title = "Para ti")
                Column(
                    modifier = Modifier.padding(ElcheSpacing.screenMargin),
                    verticalArrangement = Arrangement.spacedBy(ElcheSpacing.xl),
                ) {
                    Text(text = "ELCHE CF".uppercase(), style = ElcheTheme.typography.displayL)

                    SectionHeader(title = "Próximo partido")

                    ElcheCard(modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Elche CF — Real Madrid", style = ElcheTheme.typography.titleM)
                        Text(text = "LaLiga · Martínez Valero", style = ElcheTheme.typography.bodyS)
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(ElcheSpacing.sm)) {
                        ElcheButton(text = "Ficha del partido", onClick = {})
                        ElcheButton(text = "Comprar entradas", onClick = {}, variant = ElcheButtonVariant.Accent)
                    }

                    Franja(modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}
