package es.elchecf.app.core.webview

/**
 * Precarga en segundo plano, a la caché real del motor del WebView, la URL que se mostrará en la
 * pestaña Tienda — así el primer `AppWebView(url = TIENDA_URL)` que se monte (cuando el usuario
 * entra a la pestaña) parte de caché en vez de red fría. No mantiene ningún WebView vivo ni ocupa
 * pantalla: es solo una petición de red de fondo, sin coste de renderizado.
 *
 * Solo se llama para Tienda (CLAUDE.md §12): precargar varias URLs a la vez sí notó impacto de
 * rendimiento en el arranque, así que Entradas se deja cargar normal, al seleccionarla el usuario.
 */
expect fun prefetchShopWebView(url: String)
