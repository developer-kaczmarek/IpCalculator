plugins {
    alias(libs.plugins.convetion.library)
}

android {
    namespace = "io.github.kaczmarek.ipcalculator.core.datastore"
}

dependencies {
    api(project(":core:model"))
}