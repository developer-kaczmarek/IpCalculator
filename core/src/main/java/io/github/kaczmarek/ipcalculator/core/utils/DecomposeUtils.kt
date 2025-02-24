package io.github.kaczmarek.ipcalculator.core.utils

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

fun LifecycleOwner.componentCoroutineScope(
    exceptionHandler: CoroutineExceptionHandler? = null,
): CoroutineScope {
    val context = if (exceptionHandler != null) {
        SupervisorJob() + Dispatchers.Main.immediate + exceptionHandler
    } else {
        SupervisorJob() + Dispatchers.Main.immediate
    }

    val scope = CoroutineScope(context)

    if (lifecycle.state != Lifecycle.State.DESTROYED) {
        lifecycle.doOnDestroy {
            scope.cancel()
        }
    } else {
        scope.cancel()
    }

    return scope
}