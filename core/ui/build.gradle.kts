plugins {
    alias(libs.plugins.convention.library)
}

android {
    namespace = "io.github.kaczmarek.ipcalculator.core.ui"
}

dependencies {
    implementation(project(":core:model"))
}