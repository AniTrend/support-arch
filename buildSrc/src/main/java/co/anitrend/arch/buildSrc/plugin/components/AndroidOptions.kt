package co.anitrend.arch.buildSrc.plugin.components

import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.get
import org.jetbrains.dokka.gradle.DokkaTask
import co.anitrend.arch.buildSrc.plugin.extensions.baseExtension
import co.anitrend.arch.buildSrc.plugin.core
import co.anitrend.arch.buildSrc.plugin.data
import co.anitrend.arch.buildSrc.plugin.ext
import co.anitrend.arch.buildSrc.plugin.ui
import co.anitrend.arch.buildSrc.plugin.recycler
import co.anitrend.arch.buildSrc.plugin.domain
import co.anitrend.arch.buildSrc.plugin.theme
import co.anitrend.arch.buildSrc.plugin.extensions.androidExtensionsExtension
import co.anitrend.arch.buildSrc.plugin.extensions.containsAndroidPlugin
import co.anitrend.arch.buildSrc.plugin.extensions.publishingExtension
import co.anitrend.arch.buildSrc.plugin.extensions.libraryExtension
import co.anitrend.arch.buildSrc.common.Versions
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.kotlin.dsl.getValue
import java.io.File
import java.net.URL

private fun Project.dependenciesOfProject(): List<String> {
    return when (project.name) {
        core -> listOf(ext, data, domain)
        data -> listOf(ext, domain)
        recycler -> listOf(ext, core, theme, domain)
        ui -> listOf(ext, core, theme, domain, recycler)
        else -> emptyList()
    }
}

@Suppress("UnstableApiUsage")
internal fun Project.configureOptions() {
    if (containsAndroidPlugin()) {
        println("Applying extension options for ${project.path}")

        androidExtensionsExtension().isExperimental = true

        val baseExt = baseExtension()
        val mainSourceSet = baseExt.sourceSets["main"].java.srcDirs

        println("Applying additional tasks options for dokka and javadoc on ${project.path}")

        val dokka = tasks.withType(DokkaTask::class.java) {
            outputFormat = "html"
            outputDirectory = "$buildDir/docs/javadoc"

            subProjects = dependenciesOfProject()
            configuration {
                moduleName = project.name
                reportUndocumented = true
                platform = "JVM"
                jdkVersion = 8

                perPackageOption {
                    prefix = "kotlin"
                    skipDeprecated = false
                    reportUndocumented = true
                    includeNonPublic = false
                }

                sourceLink {
                    path = "src/main/kotlin"
                    url =
                        "https://github.com/anitrend/support-arch/tree/develop/${project.name}/src/main/kotlin"
                    lineSuffix = "#L"
                }

                externalDocumentationLink {
                    url = URL("https://developer.android.com/reference/kotlin/")
                    packageListUrl =
                        URL("https://developer.android.com/reference/androidx/package-list")
                }
            }
        }

        val dokkaJar by tasks.register("dokkaJar", Jar::class.java) {
            archiveClassifier.set("javadoc")
            from(dokka)
        }

        val sourcesJar by tasks.register("sourcesJar", Jar::class.java) {
            archiveClassifier.set("sources")
            from(mainSourceSet)
        }

        val classesJar by tasks.register("classesJar", Jar::class.java) {
            from("${project.buildDir}/intermediates/classes/release")
        }

        val javadoc = tasks.create("javadoc", Javadoc::class.java) {
            //setSource(mainSourceSet)
            classpath += project.files(baseExt.bootClasspath.joinToString(File.pathSeparator))
            libraryExtension().libraryVariants.forEach { variant ->
                if (variant.name == "release") {
                    classpath += variant.javaCompileProvider.get().classpath
                }
            }
            exclude("**/R.html", "**/R.*.html", "**/index.html")
        }

        val javadocJar = tasks.create("javadocJar", Jar::class.java) {
            dependsOn(javadoc, dokka)
            archiveClassifier.set("javadoc")
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
            includeEmptyDirs = false
            from(javadoc.destinationDir, dokka.first().outputDirectory)
        }

        artifacts {
            add("archives", dokkaJar)
            add("archives", classesJar)
            add("archives", sourcesJar)
        }

        publishingExtension().publications {
            val component = components.findByName("android")

            println("Configuring maven publication options for ${project.path}:maven with component-> ${component?.name}")
            create("maven", MavenPublication::class.java) {
                groupId = "co.anitrend.arch"
                artifactId = project.name
                version = Versions.versionName

                artifact(javadocJar)
                artifact(sourcesJar)
                artifact("${project.buildDir}/outputs/aar/${project.name}-release.aar")
                from(component)

                pom {
                    name.set("Support Arch Library")
                    description.set("A multi-module template library that attempts to make clean arch apps easier to build")
                    url.set("https://github.com/anitrend/support-arch")
                    licenses {
                        license {
                            name.set("Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("wax911")
                            name.set("Maxwell Mapako")
                            email.set("mxt.developer@gmail.com")
                            organizationUrl.set("https://github.com/anitrend")
                        }
                    }
                }
            }
        }
    }
}