package io.github.kaczmarek.ipcalculator.feature.root.di

import android.content.Context
import android.content.res.Resources
import com.arkivanov.decompose.ComponentContext
import io.github.kaczmarek.ipcalculator.core.factory.component.ComponentFactory
import io.github.kaczmarek.ipcalculator.core.manager.locale.LanguageManager
import io.github.kaczmarek.ipcalculator.core.manager.resource.ResourceManager
import io.github.kaczmarek.ipcalculator.feature.info.domain.model.AppLinkType
import io.github.kaczmarek.ipcalculator.feature.root.presentation.DefaultRootComponent
import io.github.kaczmarek.ipcalculator.feature.root.presentation.RootComponent
import io.github.kaczmarek.ipcalculator.feature.settings.domain.repository.SettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.get
import org.koin.dsl.module
import java.util.Locale

val rootModule = module {
    single { LanguageManager(context = androidContext()) }

    factory {
        getResourceManager(
            context = androidContext(),
            settingsRepository = get(),
        )
    }
}

fun ComponentFactory.createRootComponent(
    componentContext: ComponentContext,
    onOpenLink: (AppLinkType) -> Unit,
    onShareText: (String) -> Unit,
    onRateApp: () -> Unit,
): RootComponent {
    return DefaultRootComponent(
        componentContext = componentContext,
        onOpenLink = onOpenLink,
        onShareText = onShareText,
        onRateApp = onRateApp,
        settingsRepository = get(),
        languageManager = get(),
        componentFactory = get(),
    )
}

private fun getResourceManager(
    context: Context,
    settingsRepository: SettingsRepository,
): ResourceManager {
    val languageCode = settingsRepository.getSelectedLanguage().code

    return ResourceManager(context.getResources(Locale(languageCode)))
}

private fun Context.getResources(locale: Locale): Resources {
    val configuration = android.content.res.Configuration(resources.configuration)
    configuration.setLocale(locale)

    return createConfigurationContext(configuration).resources
}