package io.github.kaczmarek.ipcalculator.core.data

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.ConfigurationCompat
import androidx.core.os.LocaleListCompat
import io.github.kaczmarek.ipcalculator.core.datastore.SettingsDataStore
import io.github.kaczmarek.ipcalculator.core.model.Language

internal class DefaultLanguageRepository(
    private val settingsDataStore: SettingsDataStore,
    private val context: Context,
) : LanguageRepository {

    override fun isManualChosen(): Boolean {
        return settingsDataStore.isManualLanguageChosen()
    }

    override fun setManualChosen() {
        settingsDataStore.setManualLanguageChosen()
    }

    override fun resetManualChosen() {
        settingsDataStore.resetManualLanguageChosen()
    }

    override fun getSelectedLanguage(): Language {
        val code = settingsDataStore.getSelectedLanguageCode()

        return Language.entries.find { it.code == code } ?: Language.English
    }

    override fun setSelectedLanguage(language: Language) {
        settingsDataStore.setSelectedLanguageCode(language.code)
    }

    override fun updateLanguageConfiguration(language: Language) {
        val locales = LocaleListCompat.forLanguageTags(language.code)
        AppCompatDelegate.setApplicationLocales(locales)
    }

    override fun getSystemLocaleOrDefault(): Language {
        val currentLocale = ConfigurationCompat.getLocales(context.resources.configuration)[0]

        return Language.entries.find { it.code == currentLocale?.language } ?: Language.English
    }

    override fun isChosenFromPerAppMenu(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val localeManager = context.getSystemService(LocaleManager::class.java)
            val appLocales = localeManager.applicationLocales

            appLocales.isEmpty.not()
        } else {
            false
        }
    }
}