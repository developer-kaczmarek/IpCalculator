plugins {
    alias(libs.plugins.convention.library)
}

android {
    namespace = "io.github.kaczmarek.ipcalculator.core.datastore"
}

dependencies {
    api(project(":core:model"))
}