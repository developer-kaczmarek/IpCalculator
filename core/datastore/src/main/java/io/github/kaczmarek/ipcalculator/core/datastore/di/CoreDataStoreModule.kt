package io.github.kaczmarek.ipcalculator.core.datastore.di

import io.github.kaczmarek.ipcalculator.core.datastore.SettingsDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreDataStoreModule = module {

    single {
        SettingsDataStore(
            context = androidContext(),
        )
    }
}
