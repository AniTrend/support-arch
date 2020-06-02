package co.anitrend.arch.buildSrc.plugin.componets

import org.gradle.api.Project

internal fun Project.configurePlugins() {
    plugins.apply("com.android.library")
    plugins.apply("kotlin-android")
    plugins.apply("kotlin-android-extensions")
    plugins.apply("org.jetbrains.dokka")
}