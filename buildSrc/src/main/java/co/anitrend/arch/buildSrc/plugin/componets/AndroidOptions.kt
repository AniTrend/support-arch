package co.anitrend.arch.buildSrc.plugin.componets

import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.get
import org.jetbrains.dokka.gradle.DokkaTask
import co.anitrend.arch.buildSrc.plugin.extensions.baseExtension
import co.anitrend.arch.buildSrc.plugin.extensions.kotlinAndroidProjectExtension
import co.anitrend.arch.buildSrc.plugin.extensions.androidExtensionsExtension
import co.anitrend.arch.buildSrc.plugin.extensions.containsAndroidPlugin
import co.anitrend.arch.buildSrc.plugin.extensions.publishingExtension
import co.anitrend.arch.buildSrc.plugin.extensions.libraryExtension
import co.anitrend.arch.buildSrc.common.Versions
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.getValue
import java.net.URL

@Suppress("UnstableApiUsage")
internal fun Project.configureOptions() {
    if (containsAndroidPlugin()) {
        println("Applying extension options for ${project.path}")

        androidExtensionsExtension().isExperimental = true

        val baseExt = baseExtension()
        val mainSourceSet = baseExt.sourceSets["main"].java.srcDirs

        println("Applying additional tasks options for dokka and javadoc on ${project.path}")

        val dokka = tasks.withType(DokkaTask::class.java) {
            outputFormat = "javadoc"
            outputDirectory = "$buildDir/docs/javadoc"

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

        /*val javadoc = tasks.create("javadoc", Javadoc::class.java) {
            source = fileTree(mainSourceSet)
            classpath += project.files(extension.bootClasspath.joinToString(File.pathSeparator))
        }*/

        /*val javadocJar = tasks.create("javadocJar", Jar::class.java) {
            dependsOn(javadoc)
            archiveClassifier.set("javadoc")
            from(javadoc.destinationDir)
        }*/

        artifacts {
            add("archives", dokkaJar)
            add("archives", classesJar)
            //add("archives", javadocJar)
            add("archives", sourcesJar)
        }

        afterEvaluate {
            publishingExtension().publications {
                libraryExtension().libraryVariants
                val component = components.findByName("android")

                println("Configuring maven publication options for ${project.path}:maven with component-> ${component?.name}")
                create("maven", MavenPublication::class.java) {
                    groupId = "co.anitrend.arch"
                    artifactId = project.name
                    version = Versions.versionName

                    artifact(sourcesJar)
                    artifact("${project.buildDir}/outputs/aar/release/${project.name}.aar")
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
}