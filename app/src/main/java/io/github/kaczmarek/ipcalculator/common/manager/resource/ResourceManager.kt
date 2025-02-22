package io.github.kaczmarek.ipcalculator.common.manager.resource

import android.content.Context
import androidx.annotation.StringRes

class ResourceManager(private val context: Context) {

    fun getString(@StringRes resource: Int, vararg formatArgs: Any?): String {
        return context.resources.getString(resource, *formatArgs)
    }
}
