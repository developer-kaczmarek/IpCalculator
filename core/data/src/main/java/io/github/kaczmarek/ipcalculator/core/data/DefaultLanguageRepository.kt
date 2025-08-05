package io.github.kaczmarek.ipcalculator.core.data

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.ConfigurationCompat
import androidx.core.os.LocaleListCompat
import io.github.kaczmarek.ipcalculator.core.datastore.SettingsDataStore
import io.github.kaczmarek.ipcalculator.core.model.Language

internal class DefaultLanguageRepository(
    private val settingsDataStore: SettingsDataStore,
    private val context: Context,
) : LanguageRepository {

    override fun isLanguageSelected(): Boolean {
        val code = settingsDataStore.getSelectedLanguageCode()

        return Language.entries.find { it.code == code } != null
    }

    override fun getSelectedLanguage(): Language {
        val code = settingsDataStore.getSelectedLanguageCode()

        return Language.entries.find { it.code == code } ?: Language.English
    }

    override fun setSelectedLanguage(language: Language) {
        settingsDataStore.setSelectedLanguageCode(language.code)
        val locales = LocaleListCompat.forLanguageTags(language.code)
        AppCompatDelegate.setApplicationLocales(locales)
    }

    override fun getSystemLocaleOrDefault(): Language {
        val currentLocale = ConfigurationCompat.getLocales(context.resources.configuration)[0]

        return Language.entries.find { it.code == currentLocale?.language } ?: Language.English
    }
}