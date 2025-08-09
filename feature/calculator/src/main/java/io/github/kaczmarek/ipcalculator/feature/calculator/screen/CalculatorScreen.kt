package io.github.kaczmarek.ipcalculator.feature.calculator.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.kaczmarek.ipcalculator.core.model.AppLinkType
import io.github.kaczmarek.ipcalculator.core.model.LayoutType
import io.github.kaczmarek.ipcalculator.core.ui.theme.AppTheme
import io.github.kaczmarek.ipcalculator.core.ui.utils.isLandscapeOrientation
import io.github.kaczmarek.ipcalculator.core.ui.utils.testTagAsId
import io.github.kaczmarek.ipcalculator.core.ui.widget.SnackbarHost
import io.github.kaczmarek.ipcalculator.feature.calculator.widget.CompactContainer
import io.github.kaczmarek.ipcalculator.feature.calculator.widget.SpaciousContainer
import io.github.kaczmarek.ipcalculator.feature.calculator.widget.SubnetMaskListDialogWidget
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun CalculatorRoute(
    layoutType: LayoutType,
    onShareText: (String) -> Unit,
    onOpenLink: (AppLinkType) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CalculatorViewModel = koinViewModel(),
) {
    val uiState: CalculatorUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val shareText = remember(uiState.calculations) {
        buildString {
            uiState.calculations.forEach {
                append(it.name.asString(context))
                append(": ${it.value.asString(context)}\n")
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is CalculatorEffect.ShowErrorSnackbar -> {
                    val result = snackbarHostState.showSnackbar(
                        message = effect.message.asString(context),
                        actionLabel = effect.action.asString(context),
                        duration = SnackbarDuration.Long,
                    )

                    if (result == SnackbarResult.ActionPerformed) {
                        onOpenLink.invoke(AppLinkType.Support)
                    }
                }
            }
        }
    }

    CalculatorScreen(
        uiState = uiState,
        layoutType = layoutType,
        onOctetChange = viewModel::onOctetChange,
        onOctetDeleteImeClick = viewModel::onOctetDeleteImeClick,
        onOctetNextImeActionClick = viewModel::onOctetNextImeActionClick,
        onOctetFocusChange = viewModel::onOctetFocusChange,
        onCIDRClick = viewModel::onCIDRClick,
        onCalculateClick = viewModel::onCalculateClick,
        onShareClick = { onShareText(shareText) },
        onSubnetMaskListDialogDismissRequest = viewModel::onSubnetMaskListDialogDismissRequest,
        onSubnetMaskItemClick = viewModel::onSubnetMaskItemClick,
        modifier = modifier,
        snackbarHostState = snackbarHostState,
    )
}

@Composable
private fun CalculatorScreen(
    uiState: CalculatorUiState,
    layoutType: LayoutType,
    snackbarHostState: SnackbarHostState,
    onOctetChange: (Int, TextFieldValue) -> Unit,
    onOctetDeleteImeClick: (Int) -> Unit,
    onOctetNextImeActionClick: (Int) -> Unit,
    onOctetFocusChange: (Int) -> Unit,
    onCIDRClick: () -> Unit,
    onCalculateClick: () -> Unit,
    onShareClick: () -> Unit,
    onSubnetMaskListDialogDismissRequest: () -> Unit,
    onSubnetMaskItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background),
    ) {
        if (isLandscapeOrientation()) {
            SpaciousContainer(
                uiState = uiState,
                onOctetChange = onOctetChange,
                onOctetDeleteImeClick = onOctetDeleteImeClick,
                onOctetNextImeActionClick = onOctetNextImeActionClick,
                onOctetFocusChange = onOctetFocusChange,
                onCIDRClick = onCIDRClick,
                onCalculateClick = onCalculateClick,
                onShareClick = onShareClick,
                layoutType = layoutType,
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            CompactContainer(
                uiState = uiState,
                onOctetChange = onOctetChange,
                onOctetDeleteImeClick = onOctetDeleteImeClick,
                onOctetNextImeActionClick = onOctetNextImeActionClick,
                onOctetFocusChange = onOctetFocusChange,
                onCIDRClick = onCIDRClick,
                onCalculateClick = onCalculateClick,
                onShareClick = onShareClick,
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding(),
            )
        }

        SnackbarHost(
            snackbarHostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }

    if (uiState.isSubnetMaskListOpening) {
        SubnetMaskListDialogWidget(
            onDismissRequest = onSubnetMaskListDialogDismissRequest,
            onSubnetMaskItemClick = onSubnetMaskItemClick,
            modifier = Modifier
                .testTagAsId("SubnetMaskListDialogWidget")
                .padding(all = 16.dp)
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(24.dp),
                )
                .clip(shape = RoundedCornerShape(24.dp)),
        )
    }
}

@Preview(showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CalculatorScreenPreview() {
    AppTheme {
        val snackbarHostState = remember { SnackbarHostState() }

        CalculatorScreen(
            layoutType = LayoutType.Compat,
            onOctetChange = { _, _ -> },
            onOctetDeleteImeClick = {},
            onOctetNextImeActionClick = {},
            onOctetFocusChange = {},
            onCIDRClick = {},
            onCalculateClick = {},
            onShareClick = {},
            onSubnetMaskListDialogDismissRequest = {},
            onSubnetMaskItemClick = {},
            uiState = CalculatorUiState(),
            snackbarHostState = snackbarHostState,
        )
    }
}
