package io.github.kaczmarek.ipcalculator.core.datastore

import android.content.Context

private const val SETTINGS_PREFERENCES = "SETTINGS_PREFERENCES"
private const val APP_THEME_SETTINGS_KEY = "APP_THEME_SETTINGS_KEY"

class SettingsDataStore(context: Context) {
    private val settingsPreferences =
        context.getSharedPreferences(SETTINGS_PREFERENCES, Context.MODE_PRIVATE)

    private val preferencesEditor = settingsPreferences.edit()

    fun getSelectedThemeTypeName(): String? {
        return settingsPreferences.getString(APP_THEME_SETTINGS_KEY, null)
    }

    fun setSelectedThemeTypeName(themeTypeName: String) {
        preferencesEditor
            .putString(APP_THEME_SETTINGS_KEY, themeTypeName)
            .apply()
    }
}