package io.github.kaczmarek.ipcalculator

import android.app.Application
import android.content.Context
import io.github.kaczmarek.ipcalculator.core.factory.component.ComponentFactory
import io.github.kaczmarek.ipcalculator.core.provider.koin.KoinProvider
import io.github.kaczmarek.ipcalculator.feature.calculator.di.calculatorModule
import io.github.kaczmarek.ipcalculator.root.di.rootModule
import io.github.kaczmarek.ipcalculator.feature.settings.di.settingsModule
import org.koin.core.Koin
import org.koin.core.module.Module

class App : Application(), KoinProvider {

    override lateinit var koin: Koin
        private set

    override fun onCreate() {
        super.onCreate()

        koin = createKoin()
    }

    private fun createKoin(): Koin {
        return Koin().apply {
            loadModules(getFeatureModules())
            declare(this@App as Application)
            declare(this@App as Context)
            declare(ComponentFactory(this))
            createEagerInstances()
        }
    }

    private fun getFeatureModules(): List<Module> {
        return listOf(
            rootModule,
            calculatorModule,
            settingsModule,
        )
    }
}