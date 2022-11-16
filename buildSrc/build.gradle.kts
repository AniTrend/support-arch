import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile

plugins {
    `kotlin-dsl`
    `maven-publish`
    `version-catalog`
}

repositories {
    google()
    jcenter()
    mavenCentral()
    gradlePluginPortal()
    maven {
        setUrl("https://plugins.gradle.org/m2/")
    }
}

tasks.withType(KotlinCompile::class) {
    sourceCompatibility = "11"
    targetCompatibility = "11"
}

tasks.withType(KotlinJvmCompile::class) {
    kotlinOptions {
        jvmTarget = "11"
    }
}

fun Project.library(alias: String) =
    extensions.getByType<VersionCatalogsExtension>()
        .named("libs")
        .findLibrary(alias)
        .get()

dependencies {
    /* Depend on the android gradle plugin, since we want to access it in our plugin */
    implementation(library("android-gradle-plugin"))

    /* Depend on the kotlin plugin, since we want to access it in our plugin */
    implementation(library("jetbrains-kotlin-gradle"))

    /* Depend on the dokka plugin, since we want to access it in our plugin */
    implementation(library("jetbrains-dokka-gradle"))

    /** Dependency management */
    implementation(library("gradle-versions"))

    /** Spotless */
    implementation(library("spotless-gradle"))

    /* Depend on the default Gradle API's since we want to build a custom plugin */
    implementation(gradleApi())
    implementation(localGroovy())
}