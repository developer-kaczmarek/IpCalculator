package io.github.kaczmarek.ipcalculator.feature.calculator.presentation.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.kaczmarek.ipcalculator.core.model.layout.LayoutType
import io.github.kaczmarek.ipcalculator.core.ui.theme.AppTheme
import io.github.kaczmarek.ipcalculator.core.utils.isLandscapeOrientation
import io.github.kaczmarek.ipcalculator.feature.calculator.presentation.widget.common.SubnetMaskListDialogWidget
import io.github.kaczmarek.ipcalculator.feature.calculator.presentation.widget.compat.CompactContainer
import io.github.kaczmarek.ipcalculator.feature.calculator.presentation.widget.spacious.SpaciousContainer

@Composable
fun CalculatorScreen(
    component: CalculatorComponent,
    layoutType: LayoutType,
    modifier: Modifier = Modifier,
) {
    val uiState: CalculatorUiState by component.uiState.collectAsStateWithLifecycle()

    Box(modifier = modifier) {
        if (isLandscapeOrientation()) {
            SpaciousContainer(
                uiState = uiState,
                component = component,
                layoutType = layoutType,
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            CompactContainer(
                uiState = uiState,
                component = component,
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding(),
            )
        }
    }

    if (uiState.isSubnetMaskListOpening) {
        SubnetMaskListDialogWidget(
            onDismissRequest = component::onSubnetMaskListDialogDismissRequest,
            onSubnetMaskItemClick = component::onSubnetMaskItemClick,
            modifier = Modifier
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
        CalculatorScreen(
            layoutType = LayoutType.COMPACT,
            component = PreviewCalculatorComponent(),
        )
    }
}
