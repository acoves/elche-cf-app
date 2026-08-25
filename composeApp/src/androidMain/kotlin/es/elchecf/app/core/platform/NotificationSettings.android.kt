package es.elchecf.app.core.platform

import android.content.Intent
import android.provider.Settings
import es.elchecf.app.core.webview.androidAppContext

actual fun openNotificationSettings() {
    val intent =
        Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, androidAppContext.packageName)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    androidAppContext.startActivity(intent)
}
