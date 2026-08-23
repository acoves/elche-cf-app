package es.elchecf.app.core.webview

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * Se asigna en `ElcheApplication.onCreate()` (módulo androidApp, fuera de este módulo) antes de
 * que se use el WebView — no puede ser `internal`, androidApp necesita poder asignarla.
 */
lateinit var androidAppContext: Context

actual fun openUrlExternally(url: String) {
    val intent =
        Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    androidAppContext.startActivity(intent)
}
