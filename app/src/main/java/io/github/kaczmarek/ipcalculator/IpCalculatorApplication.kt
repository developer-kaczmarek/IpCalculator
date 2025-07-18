package io.github.kaczmarek.ipcalculator

import android.app.Application
import io.github.kaczmarek.ipcalculator.core.data.di.coreDataModule
import io.github.kaczmarek.ipcalculator.core.datastore.di.coreDataStoreModule
import io.github.kaczmarek.ipcalculator.feature.calculator.di.calculatorModule
import io.github.kaczmarek.ipcalculator.feature.settings.di.settingsModule
import io.github.kaczmarek.ipcalculator.root.di.rootModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class IpCalculatorApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@IpCalculatorApplication)
            modules(
                coreDataModule,
                coreDataStoreModule,
                rootModule,
                calculatorModule,
                settingsModule,
            )
        }
    }
}