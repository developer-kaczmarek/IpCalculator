package io.github.kaczmarek.ipcalculator.core.ui.utils

import android.content.Intent
import androidx.core.net.toUri

private const val TEXT_TYPE = "text/plain"

fun getShareTextIntent(text: String): Intent {
    val intent = Intent(Intent.ACTION_SEND)
    intent.putExtra(Intent.EXTRA_TEXT, text)
    intent.type = TEXT_TYPE
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)

    return Intent.createChooser(intent, null)
}

fun getSuitableViewerIntent(link: String): Intent {
    val intent = Intent(Intent.ACTION_VIEW, link.toUri())
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)

    return Intent.createChooser(intent, null)
}