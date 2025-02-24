package io.github.kaczmarek.ipcalculator.core.ui.widget

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import io.github.kaczmarek.ipcalculator.core.ui.theme.AppTheme

@Composable
fun LargeText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
) {
    Text(
        modifier = modifier.padding(16.dp),
        text = text,
        textAlign = textAlign,
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.bodyLarge,
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LargeTextPreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background),
        ) {
            LargeText(
                text = LoremIpsum(2).values
                    .toList()
                    .first()
                    .toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp),
            )

            LargeText(
                text = LoremIpsum(10).values
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