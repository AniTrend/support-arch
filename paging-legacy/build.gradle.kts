plugins {
    id("co.anitrend.arch")
}

android {
    namespace = "co.anitrend.arch.paging.legacy"
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":extension"))
    implementation(project(":request"))

    implementation(libs.androidx.paging.common)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.runtime.ktx)
}