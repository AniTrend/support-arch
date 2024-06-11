package co.anitrend.arch.buildSrc.plugin.components

import co.anitrend.arch.buildSrc.plugin.extensions.props
import co.anitrend.arch.buildSrc.plugin.extensions.publishingExtension
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication


internal fun Project.configureMaven() {
    publishingExtension().publications {
        val component = components.findByName("java")

        logger.lifecycle("Configuring maven publication options for ${path}:maven with component -> ${component?.name}")
        create("maven", MavenPublication::class.java) {
            groupId = "co.anitrend.arch"
            artifactId = project.name
            version = props[PropertyTypes.VERSION]

            from(component)

            pom {
                name.set("support-arch")
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
                        organizationUrl.set("https://github.com/anitrend")
                    }
                }
            }
        }
    }
}