package io.github.kaczmarek.ipcalculator

import android.app.Application
import io.github.kaczmarek.ipcalculator.core.di.coreModule
import io.github.kaczmarek.ipcalculator.feature.settings.di.settingsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@App)
            modules(
                coreModule,
                settingsModule,
            )
        }
    }
}