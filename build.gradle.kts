buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.android.gradle.plugin)
        classpath(libs.jetbrains.dokka.gradle)
        classpath(libs.jetbrains.kotlin.gradle)
    }
}

apply(plugin = "org.jetbrains.dokka")

configure<org.jetbrains.dokka.gradle.DokkaExtension> {
    moduleName.set("support-arch")
    basePublicationsDirectory.set(layout.projectDirectory.dir("dokka-docs"))
}

dependencies {
    add("dokka", project(":analytics"))
    add("dokka", project(":core"))
    add("dokka", project(":data"))
    add("dokka", project(":domain"))
    add("dokka", project(":extension"))
    add("dokka", project(":paging-legacy"))
    add("dokka", project(":recycler"))
    add("dokka", project(":recycler-paging-legacy"))
    add("dokka", project(":request"))
    add("dokka", project(":theme"))
    add("dokka", project(":ui"))
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
