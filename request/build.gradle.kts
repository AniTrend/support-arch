plugins {
    id("co.anitrend.arch")
}

android {
    namespace = "co.anitrend.arch.request"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":extension"))
}