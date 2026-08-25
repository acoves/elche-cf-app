package es.elchecf.app.core.platform

import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString

// FASE 7 (iOS): pendiente de verificar en Mac.
actual fun openNotificationSettings() {
    val url = NSURL.URLWithString(UIApplicationOpenSettingsURLString) ?: return
    UIApplication.sharedApplication.openURL(url)
}
