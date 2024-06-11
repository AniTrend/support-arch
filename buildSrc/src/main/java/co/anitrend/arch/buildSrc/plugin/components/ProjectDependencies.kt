package co.anitrend.arch.buildSrc.plugin.components

import co.anitrend.arch.buildSrc.plugin.extensions.implementation
import co.anitrend.arch.buildSrc.plugin.strategy.DependencyStrategy
import co.anitrend.arch.buildSrc.plugin.extensions.containsBasePlugin
import org.gradle.api.Project

internal fun Project.configureDependencies() {
    val dependencyStrategy by lazy { DependencyStrategy(project) }

    if (containsBasePlugin()) {
        dependencies.implementation(
            fileTree("libs") {
                include("*.jar")
            }
        )
        dependencyStrategy.applyDependenciesOn(dependencies)
    }
}