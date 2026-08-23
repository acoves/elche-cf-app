package es.elchecf.app

import android.app.Application
import es.elchecf.app.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class ElcheApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@ElcheApplication)
        }
    }
}
