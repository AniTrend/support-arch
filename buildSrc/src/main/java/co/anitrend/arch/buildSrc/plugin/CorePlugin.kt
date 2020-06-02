package co.anitrend.arch.buildSrc.plugin

import co.anitrend.arch.buildSrc.plugin.componets.configureAndroid
import co.anitrend.arch.buildSrc.plugin.componets.configureDependencies
import co.anitrend.arch.buildSrc.plugin.componets.configureOptions
import co.anitrend.arch.buildSrc.plugin.componets.configurePlugins
import org.gradle.api.Plugin
import org.gradle.api.Project

open class CorePlugin : Plugin<Project> {

    /**
     * Inspecting available extensions
     */
    @Suppress("UnstableApiUsage")
    internal fun Project.availableExtensions() {
        val extensionSchema = project.extensions.extensionsSchema
        extensionSchema.forEach {
            println("Available extension: ${it.name} -> ${it.publicType}")
        }
    }

    override fun apply(project: Project) {
        project.configurePlugins()
        project.configureAndroid()
        project.configureOptions()
        project.configureDependencies()
    }
}