package io.github.kaczmarek.ipcalculator.core.data

import android.content.ComponentCallbacks
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.ConfigurationCompat
import androidx.core.os.LocaleListCompat
import io.github.kaczmarek.ipcalculator.core.model.Language
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

internal class DefaultLanguageRepository(
    private val context: Context,
) : LanguageRepository {

    override fun observeLanguage(): Flow<Language> = callbackFlow {
        val sendLanguage = {
            trySend(getSelectedLanguage())
        }

        sendLanguage()

        val listener = object : ComponentCallbacks {
            override fun onConfigurationChanged(newConfig: Configuration) {
                sendLanguage()
            }

            @Deprecated("Deprecated in Java")
            override fun onLowMemory() {
                // No-op
            }
        }

        context.registerComponentCallbacks(listener)

        awaitClose {
            context.unregisterComponentCallbacks(listener)
        }
    }.distinctUntilChanged()

    override fun getSelectedLanguage(): Language {
        val appLocales = AppCompatDelegate.getApplicationLocales()
        if (appLocales.isEmpty) return getSystemLocaleOrDefault()

        val code = appLocales[0]?.language

        return Language.entries.find { it.code == code } ?: Language.English
    }

    override fun updateLanguageConfiguration(language: Language) {
        val locales = LocaleListCompat.forLanguageTags(language.code)
        AppCompatDelegate.setApplicationLocales(locales)
    }

    private fun getSystemLocaleOrDefault(): Language {
        val currentLocale = ConfigurationCompat.getLocales(context.resources.configuration)[0]

        return Language.entries.find { it.code == currentLocale?.language } ?: Language.English
    }
}