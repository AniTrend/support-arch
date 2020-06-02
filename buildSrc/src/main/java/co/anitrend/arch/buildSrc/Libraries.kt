package co.anitrend.arch.buildSrc

import co.anitrend.arch.buildSrc.common.Versions

@Suppress("SpellCheckingInspection")
object Libraries {

    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.gradle}"
    const val dokkaGradlePlugin = "org.jetbrains.dokka:dokka-gradle-plugin:${Versions.dokka}"

    const val threeTenBp = "com.jakewharton.threetenabp:threetenabp:${Versions.threeTenBp}"

    const val junit = "junit:junit:${Versions.junit}"
    const val mockk = "io.mockk:mockk:${Versions.mockk}"

    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    object Kotlin {
        private const val version = "1.3.72"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:$version"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
    }

    object Coroutines {
        private const val version = "1.3.7"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }


    object Google {
        private const val version = "1.2.0-beta01"
        const val material = "com.google.android.material:material:$version"
        const val firebaseCore = "com.google.firebase:firebase-core:17.4.0"

        const val crashlytics = "com.google.firebase:firebase-crashlytics:17.0.0"
        const val crashlyticsGradle = "com.google.firebase:firebase-crashlytics-gradle:2.0.0"
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:1.1.0"
        const val collection = "androidx.collection:collection-ktx:1.1.0"
        // TODO: Downgrade when material integrates merge adapter and state restoration
        const val recyclerview = "androidx.recyclerview:recyclerview:1.2.0-alpha03"
        const val swiperefresh = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0-rc01"
        const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.0.0-beta4"
        const val preference = "androidx.preference:preference:1.1.0"
        const val coreKtx = "androidx.core:core-ktx:1.3.0-rc01"

        object Fragment {
            private const val version = "1.2.3"
            const val fragment = "androidx.fragment:fragment:$version"
            const val fragmentKtx = "androidx.fragment:fragment-ktx:$version"
        }

        object Test {
            private const val version = "1.2.0"
            const val core = "androidx.test:core:$version"
            const val runner = "androidx.test:runner:$version"
            const val rules = "androidx.test:rules:$version"
        }

        object Paging {
            private const val version = "2.1.2"
            const val common = "androidx.paging:paging-common-ktx:$version"
            const val runtime = "androidx.paging:paging-runtime:$version"
            const val runtimeKtx = "androidx.paging:paging-runtime-ktx:$version"
        }

        object Lifecycle {
            private const val version = "2.2.0"
            const val extensions = "androidx.lifecycle:lifecycle-extensions:$version"
            const val runTimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
            const val liveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
            const val liveDataCoreKtx = "androidx.lifecycle:lifecycle-livedata-core-ktx:$version"
        }

        object Work {
            private const val version = "2.4.0-beta01"
            const val runtimeKtx = "androidx.work:work-runtime-ktx:$version"
        }
    }
}