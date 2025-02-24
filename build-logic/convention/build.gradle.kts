plugins {
    `kotlin-dsl`
}

group = "io.github.kaczmarek.ipcalculator.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)

    // Workaround for version catalog working inside precompiled scripts
    // Issue - https://github.com/gradle/gradle/issues/15383
    compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

gradlePlugin {
    plugins {
        register("androidLibrary") {
            id = "convention.android.library"
            implementationClass = "AndroidLibraryPlugin"
        }
    }
}
