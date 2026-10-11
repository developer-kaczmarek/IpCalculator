plugins {
    alias(libs.plugins.convention.library)
}

android {
    namespace = "io.github.kaczmarek.ipcalculator.feature.settings"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:ui"))
    implementation(project(":core:data"))
}