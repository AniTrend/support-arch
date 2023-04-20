package co.anitrend.arch.buildSrc.plugin.components

import co.anitrend.arch.buildSrc.plugin.extensions.spotlessExtension
import co.anitrend.arch.buildSrc.plugin.extensions.baseExtension
import co.anitrend.arch.buildSrc.plugin.extensions.libraryExtension
import co.anitrend.arch.buildSrc.plugin.extensions.isDomainModule
import co.anitrend.arch.buildSrc.plugin.extensions.isThemeModule
import co.anitrend.arch.buildSrc.plugin.extensions.props
import co.anitrend.arch.buildSrc.plugin.extensions.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile
import java.io.File

private fun Project.configureLint() = libraryExtension().run {
    lint {
        abortOnError = false
        ignoreWarnings = false
        ignoreTestSources = true
    }
}

internal fun Project.configureSpotless(): Unit = spotlessExtension().run {
    kotlin {
        target("**/*.kt")
        targetExclude(
            "$buildDir/**/*.kt",
            "**/androidTest/**/*.kt",
            "**/test/**/*.kt",
            "bin/**/*.kt"
        )
        ktlint(libs.versions.ktlint.get()).userData(
            mapOf("android" to "true")
        )
        licenseHeaderFile(rootProject.file("spotless/copyright.kt"))
    }
}

internal fun Project.configureAndroid(): Unit = baseExtension().run {
    compileSdkVersion(33)
    defaultConfig {
        minSdk = 23
        targetSdk = 33
        versionCode = props[PropertyTypes.CODE].toInt()
        versionName = props[PropertyTypes.VERSION]
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles.add(File("consumer-rules.pro"))
    }

    libraryExtension().run {
        buildFeatures {
            viewBinding = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            isTestCoverageEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        getByName("debug") {
            isMinifyEnabled = false
            isTestCoverageEnabled = false
        }
    }

    packagingOptions {
        resources.excludes.add("META-INF/NOTICE.*")
        resources.excludes.add("META-INF/LICENSE*")
    }

    sourceSets {
        map { androidSourceSet ->
            androidSourceSet.java.srcDir(
                "src/${androidSourceSet.name}/kotlin"
            )
        }
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
        unitTests.isReturnDefaultValues = true
    }

    configureLint()

    tasks.withType(KotlinCompile::class.java) {
        kotlinOptions {
            allWarningsAsErrors = false

            // Filter out modules that won't be using coroutines
            freeCompilerArgs = if (!project.isThemeModule() || !project.isDomainModule()) {
                listOf(
                    "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                    "-opt-in=kotlinx.coroutines.FlowPreview",
                    "-opt-in=kotlin.time.ExperimentalTime",
                )
            } else {
                listOf(
                    "-opt-in=kotlin.Experimental"
                )
            }
        }
    }
}