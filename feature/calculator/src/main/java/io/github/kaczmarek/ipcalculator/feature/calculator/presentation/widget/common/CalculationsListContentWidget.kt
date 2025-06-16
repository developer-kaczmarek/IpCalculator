package io.github.kaczmarek.ipcalculator.feature.calculator.presentation.widget.common

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kaczmarek.ipcalculator.core.ui.theme.AppTheme
import io.github.kaczmarek.ipcalculator.core.ui.theme.robotoMonoFamily
import io.github.kaczmarek.ipcalculator.core.ui.widget.LargeText
import io.github.kaczmarek.ipcalculator.core.utils.isLight
import io.github.kaczmarek.ipcalculator.feature.calculator.R
import io.github.kaczmarek.ipcalculator.feature.calculator.domain.model.Calculation
import io.github.kaczmarek.ipcalculator.feature.calculator.presentation.screen.CalculatorUiState

@Composable
internal fun CalculationsListContentWidget(
    uiState: CalculatorUiState,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        if (uiState.calculations.isEmpty()) {
            EmptyStateWidget(modifier = Modifier.fillMaxSize())
        } else {
            ContentStateWidget(
                calculations = uiState.calculations,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            )
        }
    }
}

@Composable
private fun EmptyStateWidget(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(
                    id = if (MaterialTheme.colorScheme.isLight()) {
                        R.drawable.img_empty_state_light
                    } else {
                        R.drawable.img_empty_state_dark
                    }
                ),
                contentDescription = null,
                modifier = Modifier.height(100.dp),
                contentScale = ContentScale.Inside,
            )

            LargeText(
                text = stringResource(id = R.string.calculator_empty_state_text),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(all = 16.dp),
            )
        }
    }
}

@Composable
private fun ContentStateWidget(
    calculations: List<Calculation>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        calculations.forEachIndexed { index, calculation ->
            ListItem(
                headlineContent = { Text(text = calculation.name) },
                supportingContent = {
                    Text(
                        text = calculation.value,
                        fontFamily = robotoMonoFamily,
                    )
                },
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
            )

            if (index < calculations.lastIndex) {
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CalculationsListContentWidgetPreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background),
        ) {
            CalculationsListContentWidget(
                uiState = CalculatorUiState(
                    calculations = listOf(
                        Calculation(
                            name = "IP адрес",
                            value = "192.168.1.1",
                        ),
                        Calculation(
                            name = "CIDR нотация",
                            value = "24",
                        ),
                        Calculation(
                            name = "Маска подсети",
                            value = "255.255.255.0",
                        )
                    ),
                ),
            )
        }
    }
}