package io.github.kaczmarek.ipcalculator.feature.settings.data.source

import android.content.Context

private const val SETTINGS_PREFERENCES = "SETTINGS_PREFERENCES"
private const val LANGUAGE_SETTINGS_KEY = "LANGUAGE_SETTINGS_KEY"
private const val APP_THEME_SETTINGS_KEY = "APP_THEME_SETTINGS_KEY"

internal class SettingsLocalDataStore(context: Context) {
    private val settingsPreferences =
        context.getSharedPreferences(SETTINGS_PREFERENCES, Context.MODE_PRIVATE)

    private val preferencesEditor = settingsPreferences.edit()

    fun getSelectedLanguageCode(): String? {
        return settingsPreferences.getString(LANGUAGE_SETTINGS_KEY, null)
    }

    fun setSelectedLanguageCode(code: String) {
        preferencesEditor
            .putString(LANGUAGE_SETTINGS_KEY, code)
            .apply()
    }

    fun getSelectedThemeTypeName(): String? {
        return settingsPreferences.getString(APP_THEME_SETTINGS_KEY, null)
    }

    fun setSelectedThemeTypeName(themeTypeName: String) {
        preferencesEditor
            .putString(APP_THEME_SETTINGS_KEY, themeTypeName)
            .apply()
    }
}