plugins {
    id("co.anitrend.arch")
}

android {
    namespace = "co.anitrend.arch.core"
}

dependencies {
    implementation(project(":extension"))
    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(libs.google.android.material)
    implementation(libs.androidx.work.runtime.ktx)
}