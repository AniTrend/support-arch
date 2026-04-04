plugins {
    id("co.anitrend.arch")
    kotlin("jvm")
}

kotlin {
    sourceSets {
        main {
            kotlin.srcDirs("src/main/kotlin")
        }
    }
}
