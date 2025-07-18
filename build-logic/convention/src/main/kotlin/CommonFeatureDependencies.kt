import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureFeatureDependencies(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) = with(commonExtension) {
    buildFeatures.compose = true

    dependencies {
        add("implementation", libs.androidx.lifecycle.runtime.ktx)

        add("implementation", libs.androidx.activity.compose)
        add("implementation", platform(libs.androidx.compose.bom))
        add("implementation", libs.androidx.ui)
        add("implementation", libs.androidx.ui.graphics)
        add("implementation", libs.androidx.ui.tooling.preview)
        add("implementation", libs.androidx.material3)
        add("implementation", libs.androidx.adaptive.navigation)
        add("implementation", libs.androidx.lifecycle.runtime.ktx)
        add("implementation", libs.androidx.lifecycle.viewModelCompose)
        add("implementation", libs.androidx.navigation.compose)

        add("androidTestImplementation", platform(libs.androidx.compose.bom))
        add("debugImplementation", libs.androidx.ui.tooling)

        add("implementation", libs.bundles.koin)
    }
}