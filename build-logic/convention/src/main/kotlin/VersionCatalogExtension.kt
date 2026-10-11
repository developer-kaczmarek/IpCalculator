import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

internal val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun VersionCatalog.findPluginOrThrow(name: String) =
    findPlugin(name).get().get().pluginId

internal fun VersionCatalog.findVersionOrThrow(name: String) =
    findVersion(name).get().toString()