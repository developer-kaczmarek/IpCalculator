package io.github.kaczmarek.ipcalculator.feature.root.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.doOnStart
import io.github.kaczmarek.ipcalculator.common.manager.locale.LanguageManager
import io.github.kaczmarek.ipcalculator.common.model.link.AppLinkType
import io.github.kaczmarek.ipcalculator.common.model.theme.ThemeType
import io.github.kaczmarek.ipcalculator.common.utils.componentCoroutineScope
import io.github.kaczmarek.ipcalculator.feature.calculator.presentation.CalculatorComponent
import io.github.kaczmarek.ipcalculator.feature.calculator.presentation.DefaultCalculatorComponent
import io.github.kaczmarek.ipcalculator.feature.info.presentation.DefaultInfoComponent
import io.github.kaczmarek.ipcalculator.feature.info.presentation.InfoComponent
import io.github.kaczmarek.ipcalculator.feature.settings.domain.repository.SettingsRepository
import io.github.kaczmarek.ipcalculator.feature.settings.presentation.DefaultSettingsComponent
import io.github.kaczmarek.ipcalculator.feature.settings.presentation.SettingsComponent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DefaultRootComponent(
    componentContext: ComponentContext,
    private val onOpenLink: (AppLinkType) -> Unit,
    private val onShareText: (String) -> Unit,
    private val onRateApp: () -> Unit,
) : ComponentContext by componentContext, RootComponent, KoinComponent {

    private val navigation = StackNavigation<Config>()

    private val _stack =
        childStack(
            source = navigation,
            initialConfiguration = Config.Calculator,
            serializer = Config.serializer(),
            handleBackButton = true,
            childFactory = ::child,
        )

    override val stack: Value<ChildStack<*, RootComponent.Child>> = _stack

    override val themeType = MutableStateFlow(ThemeType.System)

    private val settingsRepository: SettingsRepository by inject()
    private val languageManager: LanguageManager by inject()
    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        // TODO: show snackbar
    }

    private val coroutineScope = componentCoroutineScope(exceptionHandler)

    init {
        lifecycle.doOnStart {
            setLanguagePreferenceIfNeed()
            getThemeFromPreference()
        }
    }

    override fun onCalculatorTabClick() {
        navigation.bringToFront(Config.Calculator)
    }

    override fun onSettingsTabClick() {
        navigation.bringToFront(Config.Settings)
    }

    override fun onInfoTabClick() {
        navigation.bringToFront(Config.Info)
    }

    private fun child(config: Config, componentContext: ComponentContext): RootComponent.Child =
        when (config) {
            is Config.Calculator ->
                RootComponent.Child.CalculatorChild(
                    DefaultCalculatorComponent(
                        componentContext = componentContext,
                        onOutput = ::onCalculatorOutput,
                    )
                )

            is Config.Settings ->
                RootComponent.Child.SettingsChild(
                    DefaultSettingsComponent(
                        componentContext = componentContext,
                        onOutput = ::onSettingsOutput,
                    )
                )

            is Config.Info ->
                RootComponent.Child.InfoChild(
                    DefaultInfoComponent(
                        componentContext = componentContext,
                        onOutput = ::onInfoOutput,
                    )
                )
        }

    private fun setLanguagePreferenceIfNeed() {
        coroutineScope.launch {
            if (settingsRepository.isLanguageSelected()) return@launch

            settingsRepository.setSelectedLanguage(
                language = languageManager.getSystemLocaleOrDefault(),
            )
        }
    }

    private fun getThemeFromPreference() {
        coroutineScope.launch {
            val newThemeType = settingsRepository.getSelectedThemeType()
            themeType.update { newThemeType }
        }
    }

    private fun onSettingsOutput(output: SettingsComponent.Output) {
        if (output is SettingsComponent.Output.ThemeChanged) {
            getThemeFromPreference()
        }
    }

    private fun onInfoOutput(output: InfoComponent.Output) {
        when (output) {
            is InfoComponent.Output.OpenLink -> onOpenLink(output.linkType)
            is InfoComponent.Output.ShareText -> onShareText(output.text)
            is InfoComponent.Output.RateApp -> onRateApp()
        }
    }

    private fun onCalculatorOutput(output: CalculatorComponent.Output) {
        if (output is CalculatorComponent.Output.ShareText) {
            onShareText(output.text)
        }
    }

    @Serializable
    sealed interface Config {

        @Serializable
        data object Calculator : Config

        @Serializable
        data object Settings : Config

        @Serializable
        data object Info : Config
    }
}