package io.github.kaczmarek.ipcalculator.feature.calculator.widget

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kaczmarek.ipcalculator.core.ui.theme.AppTheme
import io.github.kaczmarek.ipcalculator.core.ui.utils.toPx
import io.github.kaczmarek.ipcalculator.core.ui.widget.LargeText
import io.github.kaczmarek.ipcalculator.feature.calculator.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SubnetMaskListDialogWidget(
    onDismissRequest: () -> Unit,
    onSubnetMaskItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    val shadowHeightInPx = 8.dp.toPx()
    val isShadowVisible: Boolean by remember {
        derivedStateOf { scrollState.value.toFloat() > shadowHeightInPx }
    }

    BasicAlertDialog(onDismissRequest = onDismissRequest) {
        Column(modifier = modifier) {
            Text(
                text = stringResource(id = R.string.calculator_subnet_mask_dialog_title),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 16.dp)
                    .padding(horizontal = 16.dp),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Box(modifier = Modifier.fillMaxSize()) {
                SubnetMaskListWidget(
                    onSubnetMaskItemClick = onSubnetMaskItemClick,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState),
                )

                if (isShadowVisible) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(height = 8.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.1f),
                                        Color.Transparent,
                                    ),
                                ),
                            ),
                    )
                }
            }
        }
    }
}

@Composable
private fun SubnetMaskListWidget(
    onSubnetMaskItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        val subnetMaskList = stringArrayResource(id = R.array.calculator_subnet_masks)

        subnetMaskList.forEachIndexed { cidrValue, subnetMask ->
            LargeText(
                text = subnetMask,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSubnetMaskItemClick(cidrValue) }
                    .padding(all = 16.dp),
            )

            if (cidrValue < subnetMaskList.lastIndex) {
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SubnetMaskListDialogWidgetPreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
        ) {
            SubnetMaskListDialogWidget(
                onDismissRequest = {
                    // Do something
                },
                onSubnetMaskItemClick = {
                    // Do something
                },
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
}