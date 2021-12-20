// Top-level build file where you can add configuration options common to all sub-projects/modules.
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jetbrains.dokka.gradle.DokkaMultiModuleTask

plugins {
    id("com.github.ben-manes.versions")
    id("org.jetbrains.dokka")
}

buildscript {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath(co.anitrend.arch.buildSrc.Libraries.Android.Tools.buildGradle)
        classpath(co.anitrend.arch.buildSrc.Libraries.JetBrains.Kotlin.Gradle.plugin)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
}

tasks.create("clean", Delete::class) {
    delete(rootProject.buildDir)
}

tasks.withType(DokkaMultiModuleTask::class.java) {
    outputDirectory.set(rootProject.file("dokka-docs"))
    failOnWarning.set(false)
}

tasks.named(
	"dependencyUpdates",
	DependencyUpdatesTask::class.java
).configure {
	checkForGradleUpdate = false
	outputFormatter = "json"
	outputDir = "build/dependencyUpdates"
	reportfileName = "report"
    resolutionStrategy {
        componentSelection {
            all {
                val reject = listOf("preview", "m")
                    .map { qualifier ->
                        val pattern = "(?i).*[.-]$qualifier[.\\d-]*"
                        Regex(pattern, RegexOption.IGNORE_CASE)
                    }
                    .any { it.matches(candidate.version) }
                if (reject)
                    reject("Preview releases not wanted")
            }
        }
    }
}
