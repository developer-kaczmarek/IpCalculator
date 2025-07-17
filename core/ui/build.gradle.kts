plugins {
    alias(libs.plugins.convetion.library)
}

android {
    namespace = "io.github.kaczmarek.ipcalculator.core.ui"
}

dependencies {
    implementation(project(":core:model"))
}