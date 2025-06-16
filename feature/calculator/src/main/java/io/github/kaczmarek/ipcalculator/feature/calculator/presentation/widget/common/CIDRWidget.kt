package io.github.kaczmarek.ipcalculator.feature.calculator.presentation.widget.common

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kaczmarek.ipcalculator.core.ui.theme.AppTheme
import io.github.kaczmarek.ipcalculator.core.ui.theme.robotoMonoFamily
import io.github.kaczmarek.ipcalculator.core.utils.empty
import io.github.kaczmarek.ipcalculator.feature.calculator.presentation.model.CIDRDvo

@Composable
internal fun CIDRWidget(
    cidrPrefix: CIDRDvo,
    onCIDRClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = CircleShape,
            )
            .clip(CircleShape)
            .clickable(onClick = onCIDRClick)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (cidrPrefix.value.isEmpty()) {
            PlaceholderText(
                text = cidrPrefix.placeholder,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.0f)
                    .padding(start = 8.dp)
                    .padding(vertical = 8.dp),
            )
        } else {
            Text(
                text = cidrPrefix.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.0f)
                    .padding(start = 8.dp)
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center,
                fontFamily = robotoMonoFamily,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge,
            )
        }

        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CIDRWidgetPreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background),
        ) {
            CIDRWidget(
                cidrPrefix = CIDRDvo(
                    placeholder = "24",
                    value = String.empty,
                ),
                onCIDRClick = {
                    // Do something
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp),
            )
        }
    }
}