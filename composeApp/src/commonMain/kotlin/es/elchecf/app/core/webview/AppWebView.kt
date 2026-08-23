package es.elchecf.app.core.webview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState
import es.elchecf.app.designsystem.component.ElcheButton
import es.elchecf.app.designsystem.component.ElcheCard
import es.elchecf.app.designsystem.component.ElcheTopBar
import es.elchecf.app.designsystem.component.Franja
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
            WebView(
                state = state,
                navigator = navigator,
                modifier = Modifier.fillMaxSize(),
            )
        }
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
