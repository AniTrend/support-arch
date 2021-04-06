package co.anitrend.arch.buildSrc.plugin.components

import org.gradle.api.Project

internal fun Project.configurePlugins() {
    plugins.apply("com.android.library")
    plugins.apply("kotlin-android")
    plugins.apply("org.jetbrains.dokka")
    plugins.apply("maven-publish")
}