package io.github.kaczmarek.ipcalculator

import android.app.Application
import android.content.Context
import io.github.kaczmarek.ipcalculator.core.data.di.coreDataModule
import io.github.kaczmarek.ipcalculator.core.datastore.di.coreDataStoreModule
import io.github.kaczmarek.ipcalculator.core.ui.factory.component.ComponentFactory
import io.github.kaczmarek.ipcalculator.core.ui.koin.KoinProvider
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
            coreDataModule,
            coreDataStoreModule,
        )
    }
}