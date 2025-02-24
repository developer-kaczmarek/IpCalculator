package io.github.kaczmarek.ipcalculator.core.di

import io.github.kaczmarek.ipcalculator.core.manager.locale.LanguageManager
import io.github.kaczmarek.ipcalculator.core.manager.resource.ResourceManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreModule = module {

    single { LanguageManager(context = androidContext()) }

    factory { ResourceManager(context = androidContext()) }
}