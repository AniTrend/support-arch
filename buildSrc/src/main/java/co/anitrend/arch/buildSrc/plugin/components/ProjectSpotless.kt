package co.anitrend.arch.buildSrc.plugin.components

import co.anitrend.arch.buildSrc.plugin.extensions.libs
import co.anitrend.arch.buildSrc.plugin.extensions.spotlessExtension
import org.gradle.api.Project


internal fun Project.configureSpotless(): Unit = spotlessExtension().run {
    kotlin {
        target("**/*.kt")
        targetExclude(
            "${layout.buildDirectory.get()}/**/*.kt",
            "**/androidTest/**/*.kt",
            "**/test/**/*.kt",
            "bin/**/*.kt"
        )
        ktlint(libs.pintrest.ktlint.get().version)
        licenseHeaderFile(rootProject.file("spotless/copyright.kt"))
    }
}