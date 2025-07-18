package io.github.kaczmarek.ipcalculator

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import io.github.kaczmarek.ipcalculator.core.model.AppLinkType
import io.github.kaczmarek.ipcalculator.core.ui.utils.getShareTextIntent
import io.github.kaczmarek.ipcalculator.core.ui.utils.getSuitableViewerIntent
import io.github.kaczmarek.ipcalculator.feature.info.R
import io.github.kaczmarek.ipcalculator.root.root.RootScreen

class IpCalculatorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableRealEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            RootScreen(
                onShareText = ::shareText,
                onOpenStore = ::openStorePage,
                onOpenLink = ::openLink,
            )
        }
    }

    private fun enableRealEdgeToEdge() {
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.auto(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            )
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }
    }

    private fun openLink(appLinkType: AppLinkType) {
        startActivity(
            getSuitableViewerIntent(
                link = resources.getString(
                    when (appLinkType) {
                        AppLinkType.Github -> R.string.github_page_link
                        AppLinkType.PrivacyPolicy -> R.string.privacy_policy_link
                        AppLinkType.Support -> R.string.developer_email_link
                    }
                ),
            )
        )
    }

    private fun shareText(text: String) {
        startActivity(getShareTextIntent(text = text))
    }

    private fun openStorePage() {
        try {
            val uri = resources.getString(R.string.market_link).toUri()
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        } catch (t: Throwable) {
            startActivity(
                getSuitableViewerIntent(
                    link = resources.getString(R.string.web_google_play_link),
                )
            )
        }
    }
}
