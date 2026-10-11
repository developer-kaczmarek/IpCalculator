plugins {
    alias(libs.plugins.convention.library)
}

android {
    namespace = "io.github.kaczmarek.ipcalculator.core.data"
}

dependencies {
    api(project(":core:datastore"))
}