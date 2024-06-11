package co.anitrend.arch.buildSrc.plugin.components

import co.anitrend.arch.buildSrc.plugin.extensions.baseExtension
import co.anitrend.arch.buildSrc.plugin.extensions.isKotlinLibraryGroup
import co.anitrend.arch.buildSrc.plugin.extensions.kotlinJvmProjectExtension
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate

internal fun Project.configureSources() {
    val mainSourceSets = when {
        !isKotlinLibraryGroup() -> baseExtension().sourceSets["main"].java.srcDirs
        else -> kotlinJvmProjectExtension().sourceSets["main"].kotlin.srcDirs()
    }

    val sourcesJar by tasks.register("sourcesJar", Jar::class.java) {
        archiveClassifier.set("sources")
        from(mainSourceSets)
    }

    val classesJar by tasks.register("classesJar", Jar::class.java) {
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