package io.github.kaczmarek.ipcalculator.core.data

import android.content.res.Resources
import androidx.annotation.StringRes

class ResourceManager(
    private val resources: Resources,
) {

    fun getString(@StringRes resource: Int, vararg formatArgs: Any?): String {
        return resources.getString(resource, *formatArgs)
    }
}
