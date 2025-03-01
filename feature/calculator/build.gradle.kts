plugins {
    alias(libs.plugins.convetion.library)
}

android {
    namespace = "io.github.kaczmarek.ipcalculator.feature.calculator"
}

dependencies {
    implementation(project(":core"))
}