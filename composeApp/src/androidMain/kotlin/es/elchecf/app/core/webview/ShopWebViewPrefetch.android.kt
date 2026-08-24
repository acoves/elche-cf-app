package es.elchecf.app.core.webview

import android.os.CancellationSignal
import androidx.webkit.OutcomeReceiverCompat
import androidx.webkit.PrefetchException
import androidx.webkit.Profile
import androidx.webkit.ProfileStore
import androidx.webkit.WebViewFeature
import java.util.concurrent.Executor

actual fun prefetchShopWebView(url: String) {
    if (!WebViewFeature.isFeatureSupported(WebViewFeature.PROFILE_URL_PREFETCH)) return
    val profile = ProfileStore.getInstance().getOrCreateProfile(Profile.DEFAULT_PROFILE_NAME)
    profile.prefetchUrlAsync(
        url,
        CancellationSignal(),
        Executor { it.run() },
        object : OutcomeReceiverCompat<Void, PrefetchException> {
            // Es una optimización de fondo: si falla, AppWebView carga igual desde red al abrir
            // la pestaña, así que no hay nada que mostrar ni reintentar aquí.
            override fun onResult(result: Void?) = Unit

            override fun onError(error: PrefetchException) = Unit
        },
    )
}
