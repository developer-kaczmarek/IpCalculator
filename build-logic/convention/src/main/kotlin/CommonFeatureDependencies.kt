import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureFeatureDependencies(
    extension: LibraryExtension,
) {
    extension.buildFeatures {
        compose = true
    }
    configureCommonDependencies()
}

private fun Project.configureCommonDependencies() {
    dependencies {
        val bom = platform(libs.findLibrary("androidx-compose-bom").get())
        add("implementation", bom)
        add("androidTestImplementation", bom)

        add("implementation", libs.findLibrary("androidx-lifecycle-runtime-ktx").get())
        add("implementation", libs.findLibrary("androidx-lifecycle-viewModelCompose").get())
        add("implementation", libs.findLibrary("androidx-activity-compose").get())
        add("implementation", libs.findLibrary("androidx-ui").get())
        add("implementation", libs.findLibrary("androidx-ui-graphics").get())
        add("implementation", libs.findLibrary("androidx-ui-tooling-preview").get())
        add("implementation", libs.findLibrary("androidx-material3").get())
        add("implementation", libs.findLibrary("androidx-adaptive-navigation").get())
        add("implementation", libs.findLibrary("androidx-navigation-compose").get())
        add("implementation", libs.findLibrary("androidx-material-icons-extended").get())

        add("debugImplementation", libs.findLibrary("androidx-ui-tooling").get())
        add("implementation", libs.findBundle("koin").get())
    }
}