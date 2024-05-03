// Top-level build file where you can add configuration options common to all sub-projects/modules.
import org.jetbrains.dokka.gradle.DokkaMultiModuleTask

plugins {
    id("org.jetbrains.dokka")
}

buildscript {
    repositories {
        google()
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
        mavenCentral()
    }
}

tasks.create("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory.get())
}

tasks.withType(DokkaMultiModuleTask::class.java) {
    outputDirectory.set(rootProject.file("dokka-docs"))
    failOnWarning.set(false)
}
