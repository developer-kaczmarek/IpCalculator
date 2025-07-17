package io.github.kaczmarek.ipcalculator.core.ui.widget

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import io.github.kaczmarek.ipcalculator.core.ui.theme.AppTheme

@Composable
fun CardWrapper(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        )
    ) {
        content()
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CardWrapperPreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background),
        ) {
            CardWrapper(
                modifier = Modifier
                    .padding(all = 16.dp)
                    .fillMaxWidth(),
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    LargeText(
                        text = LoremIpsum(2).values
                            .toList()
                            .first()
                            .toString(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 16.dp),
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                    LargeText(
                        text = LoremIpsum(2).values
                            .toList()
                            .first()
                            .toString(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 16.dp),
                    )
                }
            }
        }
    }
}