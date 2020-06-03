package co.anitrend.arch.buildSrc.plugin.componets

import co.anitrend.arch.buildSrc.plugin.strategy.DependencyStrategy
import co.anitrend.arch.buildSrc.plugin.extensions.containsAndroidPlugin
import org.gradle.api.Project

internal fun Project.configureDependencies() {
    val dependencyStrategy by lazy { DependencyStrategy(project.name) }

    if (containsAndroidPlugin()) {
        dependencies.add("implementation",
            fileTree("libs") {
                include("*.jar")
            }
        )
        dependencyStrategy.applyDependenciesOn(dependencies)
    }
}