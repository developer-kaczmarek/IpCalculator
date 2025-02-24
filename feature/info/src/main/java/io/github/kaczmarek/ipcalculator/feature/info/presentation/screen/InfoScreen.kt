package io.github.kaczmarek.ipcalculator.feature.info.presentation.screen

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kaczmarek.ipcalculator.core.ui.theme.AppTheme
import io.github.kaczmarek.ipcalculator.core.ui.widget.CardWrapper
import io.github.kaczmarek.ipcalculator.core.ui.widget.LargeText
import io.github.kaczmarek.ipcalculator.feature.info.R

@Composable
fun InfoScreen(
    component: InfoComponent,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(space = 24.dp),
    ) {
        CardWrapper(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 24.dp)
                .fillMaxWidth(),
        ) {
            LargeText(
                text = stringResource(id = R.string.info_go_to_github),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = component::onOpenGithubPageClick),
            )
        }

        CardWrapper(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        ) {
            LargeText(
                text = stringResource(id = R.string.info_privacy_policy),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = component::onReadPrivacyPolicyClick),
            )
        }

        CardWrapper(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        ) {
            val context = LocalContext.current

            Column(modifier = Modifier.fillMaxWidth()) {
                LargeText(
                    text = stringResource(id = R.string.info_send_email),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = component::onContactDeveloperClick),
                )

                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                LargeText(
                    text = stringResource(id = R.string.info_app_rate),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = component::onRateAppClick),
                )

                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                LargeText(
                    text = stringResource(id = R.string.info_share_app),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            component.onShareAppClick(
                                text = context.getString(R.string.share_app_text),
                            )
                        },
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun InfoScreenPreview() {
    AppTheme {
        InfoScreen(PreviewInfoComponent())
    }
}
