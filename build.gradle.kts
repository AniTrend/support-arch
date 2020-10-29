// Top-level build file where you can add configuration options common to all sub-projects/modules.
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    id("com.github.ben-manes.versions")
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

tasks {
    val clean by registering(Delete::class) {
        delete(rootProject.buildDir)
    }
}

tasks.named(
	"dependencyUpdates",
	DependencyUpdatesTask::class.java
).configure {
	checkForGradleUpdate = false
	outputFormatter = "json"
	outputDir = "build/dependencyUpdates"
	reportfileName = "report"
}
