package io.github.kaczmarek.ipcalculator.common.di

import io.github.kaczmarek.ipcalculator.common.manager.locale.LanguageManager
import io.github.kaczmarek.ipcalculator.common.manager.resource.ResourceManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val commonModule = module {

    single { LanguageManager(context = androidContext()) }

    factory { ResourceManager(context = androidContext()) }
}