package es.elchecf.app.core.webview

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

// FASE 6 (iOS): pendiente de verificar en Mac.
actual fun openUrlExternally(url: String) {
    val nsUrl = NSURL.URLWithString(url) ?: return
    UIApplication.sharedApplication.openURL(nsUrl)
}
