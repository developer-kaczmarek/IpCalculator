package io.github.kaczmarek.ipcalculator.root.di

import com.arkivanov.decompose.ComponentContext
import io.github.kaczmarek.ipcalculator.core.model.AppLinkType
import io.github.kaczmarek.ipcalculator.core.ui.factory.component.ComponentFactory
import io.github.kaczmarek.ipcalculator.root.presentation.DefaultRootComponent
import io.github.kaczmarek.ipcalculator.root.presentation.RootComponent
import org.koin.core.component.get

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
        languageRepository = get(),
        themeRepository = get(),
        componentFactory = get(),
    )
}
