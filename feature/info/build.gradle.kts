plugins {
    alias(libs.plugins.convetion.library)
}

android {
    namespace = "io.github.kaczmarek.ipcalculator.feature.info"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:ui"))
}