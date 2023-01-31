plugins {
    id("co.anitrend.arch")
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":extension"))

    implementation(libs.androidx.paging.common)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.runtime.ktx)
}