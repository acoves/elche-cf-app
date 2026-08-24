package es.elchecf.app.core.webview

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState
import es.elchecf.app.designsystem.component.ElcheButton
import es.elchecf.app.designsystem.component.ElcheCard
import es.elchecf.app.designsystem.component.ElcheTopBar
import es.elchecf.app.designsystem.component.Franja
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme

/**
 * Envoltorio propio sobre compose-webview-multiplatform (CLAUDE.md §3/§5.4): las pantallas de
 * Tienda nunca llaman a la librería directamente, así se puede cambiar de librería sin tocarlas.
 * Barra superior con título y atrás (navega dentro del WebView si hay historial), franja de
 * carga, y error con reintento. `mailto:`/`tel:` se abren fuera de la app — ver [isExternalScheme].
 */
@Composable
fun AppWebView(
    url: String,
    title: String,
    modifier: Modifier = Modifier,
) {
    val state = rememberWebViewState(url = url)
    val navigator = rememberWebViewNavigator()

    LaunchedEffect(state.lastLoadedUrl) {
        val loadedUrl = state.lastLoadedUrl
        if (loadedUrl != null && isExternalScheme(loadedUrl)) {
            if (navigator.canGoBack) navigator.navigateBack()
            openUrlExternally(loadedUrl)
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        ElcheTopBar(
            title = title,
            navigationIcon = {
                if (navigator.canGoBack) {
                    IconButton(onClick = { navigator.navigateBack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            },
        )

        val loadingState = state.loadingState
        if (loadingState is LoadingState.Loading) {
            Franja(
                modifier = Modifier.fillMaxWidth(fraction = loadingState.progress.coerceIn(0f, 1f)),
                thickness = 3.dp,
            )
        }

        if (state.errorsForCurrentRequest.isNotEmpty()) {
            WebViewErrorContent(onRetry = { navigator.reload() })
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                WebView(
                    state = state,
                    navigator = navigator,
                    modifier = Modifier.fillMaxSize(),
                )
                // El motor del WebView pinta en blanco hasta su primer frame real; este overlay
                // tapa ese blanco con algo de marca en vez de dejarlo a la vista. Se retira en
                // cuanto el estado deja de ser `Loading` (no espera a que la página termine del
                // todo, solo a que el navegador ya tenga contenido que mostrar).
                val overlayAlpha by
                    animateFloatAsState(
                        targetValue = if (loadingState is LoadingState.Loading) 1f else 0f,
                    )
                if (overlayAlpha > 0f) {
                    WebViewLoadingOverlay(modifier = Modifier.graphicsLayer { alpha = overlayAlpha })
                }
            }
        }
    }
}

@Composable
private fun WebViewLoadingOverlay(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().background(ElcheColor.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator(color = ElcheColor.Green)
        Text(
            text = "Cargando…",
            style = ElcheTheme.typography.bodyS,
            color = ElcheColor.InkMuted,
            modifier = Modifier.padding(top = ElcheSpacing.md),
        )
    }
}

@Composable
private fun WebViewErrorContent(onRetry: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(ElcheSpacing.lg)) {
        ElcheCard(modifier = Modifier.fillMaxWidth()) {
            Text(text = "No se ha podido cargar la página.", style = ElcheTheme.typography.titleM)
            ElcheButton(
                text = "Reintentar",
                onClick = onRetry,
                modifier = Modifier.fillMaxWidth().padding(top = ElcheSpacing.md),
            )
        }
    }
}
