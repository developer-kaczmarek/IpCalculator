package io.github.kaczmarek.ipcalculator.feature.root.presentation

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import io.github.kaczmarek.ipcalculator.core.model.theme.ThemeType
import io.github.kaczmarek.ipcalculator.feature.calculator.presentation.CalculatorComponent
import io.github.kaczmarek.ipcalculator.feature.info.presentation.screen.InfoComponent
import io.github.kaczmarek.ipcalculator.feature.settings.presentation.SettingsComponent
import kotlinx.coroutines.flow.StateFlow

interface RootComponent {

   val stack: Value<ChildStack<*, Child>>

   val themeType: StateFlow<ThemeType>

   fun onCalculatorTabClick()

   fun onSettingsTabClick()

   fun onInfoTabClick()

   sealed interface Child {

      class CalculatorChild(val component: CalculatorComponent) : Child

      class SettingsChild(val component: SettingsComponent) : Child

      class InfoChild(val component: io.github.kaczmarek.ipcalculator.feature.info.presentation.screen.InfoComponent) : Child
   }
}