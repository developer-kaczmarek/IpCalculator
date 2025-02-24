import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.plugins.androidLibrary.get().pluginId)
            apply(libs.plugins.jetbrainsKotlinAndroid.get().pluginId)
            apply(libs.plugins.jetbrainsKotlinComposeCompiler.get().pluginId)
            apply(libs.plugins.jetbrainsKotlinSerialization.get().pluginId)
        }

        extensions.configure<LibraryExtension> {
            configureFeatureDependencies(this)
            configureKotlinAndroid(this)
            packaging.resources.excludes += setOf(
                "/META-INF/{AL2.0,LGPL2.1}",
                "/META-INF/INDEX.LIST",
            )
        }
    }
}