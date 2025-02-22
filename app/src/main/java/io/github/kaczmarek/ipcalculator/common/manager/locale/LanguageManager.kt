package io.github.kaczmarek.ipcalculator.common.manager.locale

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.ConfigurationCompat
import androidx.core.os.LocaleListCompat
import io.github.kaczmarek.ipcalculator.common.model.language.Language

class LanguageManager(private val context: Context) {

    fun getSystemLocaleOrDefault(): Language {
        val currentLocale = ConfigurationCompat.getLocales(context.resources.configuration)[0]
        return Language.entries.find { it.code == currentLocale?.language } ?: Language.English
    }

    fun updateAppLocale(language: Language) {
        val locales = LocaleListCompat.forLanguageTags(language.code)
        AppCompatDelegate.setApplicationLocales(locales)
    }
}