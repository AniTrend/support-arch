plugins {
    id("co.anitrend.arch")
}

android {
    namespace = "co.anitrend.arch.data"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":extension"))
    implementation(project(":request"))

    implementation(libs.androidx.paging.common)
    implementation(libs.androidx.paging.common.ktx)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.runtime.ktx)
}