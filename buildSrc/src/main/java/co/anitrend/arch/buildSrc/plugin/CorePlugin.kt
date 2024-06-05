package co.anitrend.arch.buildSrc.plugin

import co.anitrend.arch.buildSrc.plugin.components.configureAndroid
import co.anitrend.arch.buildSrc.plugin.components.configureSpotless
import co.anitrend.arch.buildSrc.plugin.components.configureDependencies
import co.anitrend.arch.buildSrc.plugin.components.configureOptions
import co.anitrend.arch.buildSrc.plugin.components.configurePlugins
import co.anitrend.arch.buildSrc.plugin.extensions.isKotlinLibraryGroup
import org.gradle.api.Plugin
import org.gradle.api.Project

open class CorePlugin : Plugin<Project> {

    private fun Project.availableExtensions() {
        val extensionSchema = project.extensions.extensionsSchema
        extensionSchema.forEach {
            logger.lifecycle("Available extension for module ${project.path}: ${it.name} -> ${it.publicType}")
        }
    }

    private fun Project.availableComponents() {
        val collectionSchema = project.components.asMap
        collectionSchema.forEach {
            logger.lifecycle("Available component for module ${project.path}: ${it.key} -> ${it.value}")
        }
    }

    override fun apply(project: Project) {
        project.configurePlugins()
        if (!project.isKotlinLibraryGroup()) {
            project.configureAndroid()
        }
        project.configureOptions()
        project.configureDependencies()
        project.configureSpotless()
        project.availableExtensions()
        project.availableComponents()
    }
}