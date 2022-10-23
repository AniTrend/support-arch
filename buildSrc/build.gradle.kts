import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile

plugins {
    `kotlin-dsl`
    `maven-publish`
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

val buildToolsVersion = "7.2.1"
val kotlinVersion = "1.7.20"
val dokkaVersion = "1.6.21"
val manesVersion = "0.42.0"
val spotlessVersion = "6.11.0"

dependencies {
    /* Depend on the android gradle plugin, since we want to access it in our plugin */
    implementation("com.android.tools.build:gradle:$buildToolsVersion")

    /* Depend on the kotlin plugin, since we want to access it in our plugin */
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")

    /* Depend on the dokka plugin, since we want to access it in our plugin */
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:$dokkaVersion")

    /** Dependency management */
    implementation("com.github.ben-manes:gradle-versions-plugin:$manesVersion")

    /** Spotless */
    implementation("com.diffplug.spotless:spotless-plugin-gradle:$spotlessVersion")

    /* Depend on the default Gradle API's since we want to build a custom plugin */
    implementation(gradleApi())
    implementation(localGroovy())
}