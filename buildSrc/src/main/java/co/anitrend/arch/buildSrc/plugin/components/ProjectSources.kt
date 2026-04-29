package co.anitrend.arch.buildSrc.plugin.components

import co.anitrend.arch.buildSrc.plugin.extensions.libraryExtension
import co.anitrend.arch.buildSrc.plugin.extensions.isKotlinLibraryGroup
import co.anitrend.arch.buildSrc.plugin.extensions.kotlinJvmProjectExtension
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate

internal fun Project.configureSources() {
    val mainSourceSets = when {
        !isKotlinLibraryGroup() -> libraryExtension().sourceSets["main"].java.directories
        else -> kotlinJvmProjectExtension().sourceSets["main"].kotlin.srcDirs()
    }

    val sourcesJar by tasks.register("sourcesJar", Jar::class.java) {
        archiveClassifier.set("sources")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from(mainSourceSets)
    }

    val classesJar by tasks.register("classesJar", Jar::class.java) {
        archiveClassifier.set("classes")
        from("${project.layout.buildDirectory.get()}/intermediates/classes/release")
    }

    artifacts {
        add("archives", classesJar)
        add("archives", sourcesJar)
    }

    afterEvaluate {
        configureMaven(sourcesJar)
    }
}