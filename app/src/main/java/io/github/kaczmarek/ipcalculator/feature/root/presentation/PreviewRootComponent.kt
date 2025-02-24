package io.github.kaczmarek.ipcalculator.feature.root.presentation

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.kaczmarek.ipcalculator.feature.calculator.presentation.PreviewCalculatorComponent
import io.github.kaczmarek.ipcalculator.core.model.theme.ThemeType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PreviewRootComponent : RootComponent {

    override val stack: Value<ChildStack<*, RootComponent.Child>> =
        MutableValue(
            ChildStack(
                configuration = "<preview>",
                instance = RootComponent.Child.CalculatorChild(
                    component = PreviewCalculatorComponent(),
                ),
            )
        )

    override val themeType: StateFlow<ThemeType> = MutableStateFlow(ThemeType.System)

    override fun onCalculatorTabClick() = Unit

    override fun onSettingsTabClick() = Unit

    override fun onInfoTabClick() = Unit
}