package co.anitrend.arch.buildSrc.plugin.componets

import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.get
import org.jetbrains.dokka.gradle.DokkaTask
import co.anitrend.arch.buildSrc.plugin.extensions.baseExtension
import co.anitrend.arch.buildSrc.plugin.extensions.androidExtensionsExtension
import co.anitrend.arch.buildSrc.plugin.extensions.containsAndroidPlugin
import java.net.URL

@Suppress("UnstableApiUsage")
internal fun Project.configureOptions() {
    if (containsAndroidPlugin()) {
        androidExtensionsExtension().isExperimental = true

        println("Applying additional tasks options for ${project.path} -> ${project.projectDir}")

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

        val mainSources = baseExtension().sourceSets["main"].java.srcDirs

        val dokkaJar = tasks.create("dokkaJar", Jar::class.java) {
            archiveClassifier.set("javadoc")
            from(dokka)
        }

        val sourcesJar = tasks.create("sourcesJar", Jar::class.java) {
            archiveClassifier.set("sources")
            from(mainSources)
        }

        val classesJar = tasks.create("classesJar", Jar::class.java) {
            from("${project.buildDir}/intermediates/classes/release")
        }

        /*val javadoc = tasks.create("javadoc", Javadoc::class.java) {
            source = fileTree(mainSources)
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
    }
}