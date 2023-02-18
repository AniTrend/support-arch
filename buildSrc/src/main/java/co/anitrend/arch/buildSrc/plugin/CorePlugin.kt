package co.anitrend.arch.buildSrc.plugin

import co.anitrend.arch.buildSrc.plugin.components.configureAndroid
import co.anitrend.arch.buildSrc.plugin.components.configureSpotless
import co.anitrend.arch.buildSrc.plugin.components.configureDependencies
import co.anitrend.arch.buildSrc.plugin.components.configureOptions
import co.anitrend.arch.buildSrc.plugin.components.configurePlugins
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
            logger.lifecycle("Available extension for module ${project.path}: ${it.name} -> ${it.publicType}")
        }
    }

    /**
     * Inspecting available components
     */
    @Suppress("UnstableApiUsage")
    internal fun Project.availableComponents() {
        val collectionSchema = project.components.asMap
        collectionSchema.forEach {
            logger.lifecycle("Available component for module ${project.path}: ${it.key} -> ${it.value}")
        }
    }

    override fun apply(project: Project) {
        project.configurePlugins()
        project.configureAndroid()
        project.configureOptions()
        project.configureDependencies()
        project.configureSpotless()
        project.availableExtensions()
        project.availableComponents()
    }
}