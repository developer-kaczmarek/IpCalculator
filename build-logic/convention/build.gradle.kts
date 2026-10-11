plugins {
    `kotlin-dsl`
}

group = "io.github.kaczmarek.ipcalculator.buildlogic"

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidLibrary") {
            id = "convention.android.library"
            implementationClass = "AndroidLibraryPlugin"
        }
    }
}
