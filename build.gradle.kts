// Top-level build file where you can add configuration options common to all sub-projects/modules.
import co.anitrend.arch.buildSrc.plugin.resolver.handleDependencySelection
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
        classpath(libs.android.gradle.plugin)
        classpath(libs.jetbrains.kotlin.gradle)
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
            all { handleDependencySelection() }
        }
    }
}
