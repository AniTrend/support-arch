package co.anitrend.arch.buildSrc.plugin.components

import co.anitrend.arch.buildSrc.plugin.extensions.isKotlinLibraryGroup
import org.gradle.api.Project

internal fun Project.configurePlugins() {
    if (isKotlinLibraryGroup()) {
        plugins.apply("kotlin")
    } else {
        plugins.apply("com.android.library")
        plugins.apply("kotlin-android")
    }
    plugins.apply("com.diffplug.spotless")
    plugins.apply("org.jetbrains.dokka")
    plugins.apply("maven-publish")
}