package co.anitrend.arch.buildSrc.plugin.extensions

import co.anitrend.arch.buildSrc.module.Modules
import co.anitrend.arch.buildSrc.plugin.components.PropertiesReader
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.api.internal.plugins.DefaultArtifactPublicationSet
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.reporting.ReportingExtension
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.internal.AndroidExtensionsExtension
import org.jetbrains.kotlin.gradle.testing.internal.KotlinTestsRegistry

fun Project.hasDependencies() =
    name == Modules.Support.Core.id ||
            name == Modules.Support.Data.id ||
            name == Modules.Support.Extensions.id ||
            name == Modules.Support.Recycler.id ||
            name == Modules.Support.Ui.id ||
            name == Modules.Support.Request.id ||
            name == Modules.Support.PagingLegacy.id ||
            name == Modules.Support.RecyclerPagingLegacy.id

fun Project.isAnnotationModule() =
    name == Modules.Support.Annotation.id

fun Project.isCoreModule() =
    name == Modules.Support.Core.id

fun Project.isDataModule() =
    name == Modules.Support.Data.id

fun Project.isDomainModule() =
    name == Modules.Support.Domain.id

fun Project.isExtensionsModule() =
    name == Modules.Support.Extensions.id

fun Project.isRecyclerModule() =
    name == Modules.Support.Recycler.id

fun Project.isThemeModule() =
    name == Modules.Support.Theme.id

fun Project.isUiModule() =
    name == Modules.Support.Ui.id

fun Project.isPagingModule() =
    name == Modules.Support.PagingLegacy.id

fun Project.isRequestModule() =
    name == Modules.Support.Request.id

fun Project.isRecyclerPagingLegacyModule() =
    name == Modules.Support.RecyclerPagingLegacy.id

fun Project.isProcessorModule() =
    name == Modules.Support.Processor.id

fun Project.isKotlinLibraryGroup() = isProcessorModule() || isAnnotationModule()

internal val Project.props: PropertiesReader
    get() = PropertiesReader(this)

internal val Project.libs: LibrariesForLibs
    get() = extensions.getByType<LibrariesForLibs>()

internal fun Project.baseExtension() =
    extensions.getByType<BaseExtension>()

internal fun Project.extraPropertiesExtension() =
    extensions.getByType<ExtraPropertiesExtension>()

internal fun Project.defaultArtifactPublicationSet() =
    extensions.getByType<DefaultArtifactPublicationSet>()

internal fun Project.reportingExtension() =
    extensions.getByType<ReportingExtension>()

internal fun Project.sourceSetContainer() =
    extensions.getByType<SourceSetContainer>()

internal fun Project.javaPluginExtension() =
    extensions.getByType<JavaPluginExtension>()

internal fun Project.libraryExtension() =
    extensions.getByType<LibraryExtension>()

internal fun Project.kotlinAndroidProjectExtension() =
    extensions.getByType<KotlinAndroidProjectExtension>()

internal fun Project.kotlinTestsRegistry() =
    extensions.getByType<KotlinTestsRegistry>()


internal fun Project.androidExtensionsExtension() =
    extensions.getByType<AndroidExtensionsExtension>()

internal fun Project.publishingExtension() =
    extensions.getByType<PublishingExtension>()

internal fun Project.spotlessExtension() =
    extensions.getByType<SpotlessExtension>()

internal fun Project.kotlinJvmProjectExtension() =
    extensions.getByType<KotlinJvmProjectExtension>()

internal fun Project.containsBasePlugin(): Boolean {
    return project.plugins.toList().any { plugin ->
        plugin is BasePlugin
    }
}