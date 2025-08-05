package io.github.kaczmarek.ipcalculator.root.root

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.kaczmarek.ipcalculator.core.model.AppLinkType
import io.github.kaczmarek.ipcalculator.core.ui.theme.AppTheme
import io.github.kaczmarek.ipcalculator.core.ui.utils.getLayoutType
import io.github.kaczmarek.ipcalculator.core.ui.utils.isLandscapeOrientation
import io.github.kaczmarek.ipcalculator.core.ui.utils.navigateToBarRoute
import io.github.kaczmarek.ipcalculator.feature.calculator.navigation.CALCULATOR_ROUTE
import io.github.kaczmarek.ipcalculator.root.navigation.IpCalculatorNavGraph
import io.github.kaczmarek.ipcalculator.root.navigation.ipCalculatorTabScreens
import org.koin.androidx.compose.koinViewModel

@Composable
fun RootScreen(
    onOpenLink: (AppLinkType) -> Unit,
    onShareText: (String) -> Unit,
    onOpenStore: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RootViewModel = koinViewModel(),
) {
    val themeType by viewModel.themeType.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val layoutType = getLayoutType(adaptiveInfo)

    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route ?: CALCULATOR_ROUTE

    AppTheme(themeType) {
        Row(modifier = modifier) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                containerColor = MaterialTheme.colorScheme.background,
                bottomBar = {
                    if (!isLandscapeOrientation()) {
                        BottomBar(
                            currentRoute = currentRoute,
                            onNavigate = { navController.navigateToBarRoute(route = it) },
                        )
                    }
                }
            ) { innerPadding ->
                Row(
                    modifier = Modifier
                        .padding(innerPadding)
                        .consumeWindowInsets(innerPadding)
                        .fillMaxSize()
                ) {
                    IpCalculatorNavGraph(
                        layoutType = layoutType,
                        onOpenLink = onOpenLink,
                        onShareText = onShareText,
                        onOpenStore = onOpenStore,
                        navController = navController,
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    )

                    if (isLandscapeOrientation()) {
                        VerticalDivider()

                        Box(
                            modifier = Modifier
                                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.End))
                                .background(
                                    color = MaterialTheme.colorScheme.surface,
                                    shape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp),
                                )
                                .padding(horizontal = 8.dp)
                        ) {
                            NavigationRailBar(
                                currentRoute = currentRoute,
                                onNavigate = { navController.navigateToBarRoute(route = it) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NavigationRailBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationRail(
        modifier = modifier.testTag("RootNavigationRailBar"),
        containerColor = Color.Transparent,
    ) {
        Spacer(modifier = Modifier.weight(1.0f))

        ipCalculatorTabScreens.forEach { destination ->
            NavigationItem(
                modifier = Modifier.testTag("RootNavigationItem+${destination.route}"),
                selected = destination.route == currentRoute,
                onClick = { onNavigate(destination.route) },
                labelRes = destination.titleTextId,
                icon = destination.icon,
            )
        }
        Spacer(modifier = Modifier.weight(1.0f))
    }
}

@Composable
private fun BottomBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("RootBottomBar"),
            containerColor = MaterialTheme.colorScheme.surface,
            windowInsets = WindowInsets(0.dp),
        ) {
            ipCalculatorTabScreens.forEach { destination ->
                NavigationItem(
                    modifier = Modifier.testTag("RootNavigationItem+${destination.route}"),
                    selected = destination.route == currentRoute,
                    onClick = { onNavigate(destination.route) },
                    labelRes = destination.titleTextId,
                    icon = destination.icon,
                )
            }
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
private fun RowScope.NavigationItem(
    icon: ImageVector,
    @StringRes labelRes: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBarItem(
        modifier = modifier,
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
private fun NavigationItem(
    icon: ImageVector,
    @StringRes labelRes: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationRailItem(
        modifier = modifier,
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
            onOpenLink = {},
            onOpenStore = {},
            onShareText = {},
        )
    }
}

