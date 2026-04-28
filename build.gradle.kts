buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.android.gradle.plugin)
        classpath(libs.jetbrains.kotlin.gradle)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
