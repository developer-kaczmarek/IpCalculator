package io.github.kaczmarek.ipcalculator.root.presentation

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import io.github.kaczmarek.ipcalculator.core.model.LayoutType
import io.github.kaczmarek.ipcalculator.core.model.ThemeType
import io.github.kaczmarek.ipcalculator.core.ui.theme.AppTheme
import io.github.kaczmarek.ipcalculator.core.ui.utils.getLayoutType
import io.github.kaczmarek.ipcalculator.core.ui.utils.isLandscapeOrientation
import io.github.kaczmarek.ipcalculator.feature.calculator.presentation.screen.CalculatorScreen
import io.github.kaczmarek.ipcalculator.feature.info.presentation.screen.InfoScreen
import io.github.kaczmarek.ipcalculator.feature.settings.presentation.SettingsScreen

@Composable
fun RootScreen(
    component: RootComponent,
    modifier: Modifier = Modifier,
) {
    val themeType: ThemeType by component.themeType.collectAsStateWithLifecycle()

    AppTheme(themeType) {
        val stack by component.stack.subscribeAsState()
        val activeComponent = stack.active.instance
        val adaptiveInfo = currentWindowAdaptiveInfo()
        val layoutType = getLayoutType(adaptiveInfo = adaptiveInfo)

        Row(modifier = modifier) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1.0f),
                containerColor = MaterialTheme.colorScheme.background,
                bottomBar = {
                    if (!isLandscapeOrientation()) {
                        BottomBar(
                            component = component,
                            activeComponent = activeComponent,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                },
            ) { innerPadding ->
                RootContent(
                    layoutType = layoutType,
                    component = component,
                    activeComponent = activeComponent,
                    modifier = Modifier
                        .padding(innerPadding)
                        .consumeWindowInsets(innerPadding)
                        .fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun RootContent(
    layoutType: LayoutType,
    component: RootComponent,
    activeComponent: RootComponent.Child,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = WindowInsets.safeDrawing,
) {
    Row(modifier = modifier) {
        Children(
            component = component,
            layoutType = layoutType,
            modifier = Modifier
                .fillMaxSize()
                .weight(1.0f),
        )

        if (isLandscapeOrientation()) {
            if (activeComponent is RootComponent.Child.CalculatorChild) {
                VerticalDivider()
            }

            Box(
                modifier = Modifier
                    .windowInsetsPadding(windowInsets.only(WindowInsetsSides.End))
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = if (activeComponent is RootComponent.Child.CalculatorChild) {
                            RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp)
                        } else {
                            RoundedCornerShape(24.dp)
                        },
                    )
                    .padding(horizontal = 8.dp),
            ) {
                NavigationRailBar(
                    component = component,
                    activeComponent = activeComponent,
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }
    }
}

@Composable
private fun Children(
    component: RootComponent,
    layoutType: LayoutType,
    modifier: Modifier = Modifier,
) {
    Children(
        stack = component.stack,
        modifier = modifier,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is RootComponent.Child.CalculatorChild -> CalculatorScreen(
                component = child.component,
                layoutType = layoutType,
                modifier = Modifier.fillMaxSize(),
            )

            is RootComponent.Child.SettingsChild -> SettingsScreen(
                component = child.component,
                layoutType = layoutType,
                modifier = Modifier.fillMaxSize(),
            )

            is RootComponent.Child.InfoChild -> InfoScreen(
                component = child.component,
                layoutType = layoutType,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
fun NavigationRailBar(
    component: RootComponent,
    activeComponent: RootComponent.Child,
    modifier: Modifier = Modifier,
) {
    NavigationRail(
        modifier = modifier,
        containerColor = Color.Transparent,
    ) {
        Spacer(modifier = Modifier.weight(1.0f))
        NavigationItem(
            icon = Icons.AutoMirrored.Filled.List,
            labelRes = R.string.root_nav_calculator,
            selected = activeComponent is RootComponent.Child.CalculatorChild,
            onClick = component::onCalculatorTabClick,
        )

        NavigationItem(
            icon = Icons.Default.Settings,
            labelRes = R.string.root_nav_settings,
            selected = activeComponent is RootComponent.Child.SettingsChild,
            onClick = component::onSettingsTabClick,
        )

        NavigationItem(
            icon = Icons.Default.Info,
            labelRes = R.string.root_nav_info,
            selected = activeComponent is RootComponent.Child.InfoChild,
            onClick = component::onInfoTabClick,
        )
        Spacer(modifier = Modifier.weight(1.0f))
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

@Composable
fun NavigationItem(
    icon: ImageVector,
    @StringRes labelRes: Int,
    selected: Boolean,
    onClick: () -> Unit,
) {
    NavigationRailItem(
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
        RootScreen(
            component = PreviewRootComponent(),
        )
    }
}

