import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal fun Project.configureKotlinAndroid(
    extension: LibraryExtension,
) {
    extension.apply {
        compileSdk = libs.findVersionOrThrow("compileSdk").toInt()
        defaultConfig {
            minSdk = libs.findVersionOrThrow("minSdk").toInt()
            lint.targetSdk = libs.findVersionOrThrow("targetSdk").toInt()
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
            isCoreLibraryDesugaringEnabled = true
        }
    }
    configureKotlinCommon()
}

private fun Project.configureKotlinCommon() {
    dependencies {
        add("coreLibraryDesugaring", libs.findLibrary("android-desugarJdkLibs").get())
    }

    extensions.configure<KotlinAndroidProjectExtension> {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
            freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
        }
    }
}