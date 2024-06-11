package co.anitrend.arch.buildSrc.plugin.components

import co.anitrend.arch.buildSrc.plugin.extensions.baseExtension
import co.anitrend.arch.buildSrc.plugin.extensions.libraryExtension
import co.anitrend.arch.buildSrc.plugin.extensions.isDomainModule
import co.anitrend.arch.buildSrc.plugin.extensions.isThemeModule
import co.anitrend.arch.buildSrc.plugin.extensions.kotlinAndroidProjectExtension
import co.anitrend.arch.buildSrc.plugin.extensions.props
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask
import java.io.File

private fun Project.configureLint() = libraryExtension().run {
    lint {
        abortOnError = false
        ignoreWarnings = false
        ignoreTestSources = true
    }
}

internal fun Project.configureAndroid(): Unit = baseExtension().run {
    compileSdkVersion(34)
    defaultConfig {
        minSdk = 23
        targetSdk = 34
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    tasks.withType(KotlinCompilationTask::class.java) {
        compilerOptions {
            allWarningsAsErrors.set(false)
            // Filter out modules that won't be using coroutines
            if (!project.isThemeModule() || !project.isDomainModule()) {
                freeCompilerArgs.addAll(
                    "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                    "-opt-in=kotlinx.coroutines.FlowPreview",
                    "-opt-in=kotlin.time.ExperimentalTime",
                )
            } else {
                freeCompilerArgs.addAll(
                    "-opt-in=kotlin.Experimental"
                )
            }
        }
    }

    tasks.withType(Test::class.java) {
        useJUnitPlatform()
    }

    kotlinAndroidProjectExtension().run {
        jvmToolchain(17)
    }
}
