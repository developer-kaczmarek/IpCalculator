plugins {
    alias(libs.plugins.convetion.library)
}

android {
    namespace = "io.github.kaczmarek.ipcalculator.feature.settings"
}

dependencies {
    implementation(project(":core"))
}