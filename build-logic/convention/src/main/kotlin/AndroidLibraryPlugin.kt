import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.findPluginOrThrow("androidLibrary"))
            apply(libs.findPluginOrThrow("jetbrainsKotlinComposeCompiler"))
            apply(libs.findPluginOrThrow("jetbrainsKotlinSerialization"))
        }

        extensions.configure<LibraryExtension> {
            configureKotlinAndroid(this)
            configureFeatureDependencies(this)

            packaging {
                resources {
                    excludes += setOf(
                        "/META-INF/{AL2.0,LGPL2.1}",
                        "/META-INF/INDEX.LIST",
                    )
                }
            }
        }
    }
}