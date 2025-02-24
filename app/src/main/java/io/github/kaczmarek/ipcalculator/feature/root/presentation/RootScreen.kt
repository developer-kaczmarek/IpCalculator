package io.github.kaczmarek.ipcalculator.feature.root.presentation

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.kaczmarek.ipcalculator.R
import io.github.kaczmarek.ipcalculator.core.model.theme.ThemeType
import io.github.kaczmarek.ipcalculator.core.ui.theme.AppTheme
import io.github.kaczmarek.ipcalculator.feature.calculator.presentation.CalculatorScreen
import io.github.kaczmarek.ipcalculator.feature.info.presentation.screen.InfoScreen
import io.github.kaczmarek.ipcalculator.feature.settings.presentation.SettingsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootScreen(
    component: RootComponent,
    modifier: Modifier = Modifier,
) {
    val themeType: ThemeType by component.themeType.collectAsStateWithLifecycle()

    AppTheme(themeType) {
        val stack by component.stack.subscribeAsState()
        val activeComponent = stack.active.instance

        Scaffold(
            modifier = modifier,
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(
                                id = when (activeComponent) {
                                    is RootComponent.Child.CalculatorChild -> R.string.root_nav_calculator
                                    is RootComponent.Child.SettingsChild -> R.string.root_nav_settings
                                    is RootComponent.Child.InfoChild -> R.string.root_nav_info
                                }
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                )
            },
            bottomBar = {
                BottomBar(
                    component = component,
                    activeComponent = activeComponent,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
        ) { innerPadding ->
            Children(
                component = component,
                modifier = Modifier
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding)
                    .imePadding()
                    .fillMaxSize(),
            )
        }
    }
}

@Composable
private fun Children(component: RootComponent, modifier: Modifier = Modifier) {
    Children(
        stack = component.stack,
        modifier = modifier,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is RootComponent.Child.CalculatorChild -> CalculatorScreen(
                component = child.component,
                modifier = Modifier.fillMaxSize(),
            )

            is RootComponent.Child.SettingsChild -> SettingsScreen(
                component = child.component,
                modifier = Modifier.fillMaxSize(),
            )

            is RootComponent.Child.InfoChild -> InfoScreen(
                component = child.component,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun BottomBar(
    component: RootComponent,
    activeComponent: RootComponent.Child,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        NavigationBar(
            modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.surface,
            windowInsets = WindowInsets(0.dp),
        ) {
            NavigationItem(
                selected = activeComponent is RootComponent.Child.CalculatorChild,
                onClick = component::onCalculatorTabClick,
                labelRes = R.string.root_nav_calculator,
                icon = Icons.AutoMirrored.Filled.List,
            )

            NavigationItem(
                selected = activeComponent is RootComponent.Child.SettingsChild,
                onClick = component::onSettingsTabClick,
                labelRes = R.string.root_nav_settings,
                icon = Icons.Default.Settings,
            )

            NavigationItem(
                selected = activeComponent is RootComponent.Child.InfoChild,
                onClick = component::onInfoTabClick,
                labelRes = R.string.root_nav_info,
                icon = Icons.Default.Info,
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .windowInsetsBottomHeight(WindowInsets.navigationBars),
        )
    }
}

@Composable
fun RowScope.NavigationItem(
    icon: ImageVector,
    @StringRes labelRes: Int,
    selected: Boolean,
    onClick: () -> Unit,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = stringResource(labelRes),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        icon = { Icon(imageVector = icon, contentDescription = null) },
    )
}

@Preview(showSystemUi = true)
@Composable
private fun RootScreenPreview() {
    AppTheme {
        RootScreen(PreviewRootComponent())
    }
}

