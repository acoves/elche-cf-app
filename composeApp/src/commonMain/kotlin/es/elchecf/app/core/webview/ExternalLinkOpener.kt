package es.elchecf.app.core.webview

/**
 * Abre `mailto:`, `tel:` y similares fuera de la app (CLAUDE.md §5.4). La librería de WebView no
 * expone un `shouldOverrideUrlLoading`, así que [es.elchecf.app.core.webview.isExternalScheme] +
 * esta función son el mecanismo con el que [AppWebView] reacciona cuando la carga falla dentro
 * del WebView por no ser http(s).
 */
expect fun openUrlExternally(url: String)

/** Solo mailto:/tel: — un enlace a una pasarela de pago externa sigue siendo http(s) y no se puede
 * distinguir de un enlace normal sin `shouldOverrideUrlLoading`; se deja cargar dentro del WebView. */
fun isExternalScheme(url: String): Boolean = url.startsWith("mailto:") || url.startsWith("tel:")
