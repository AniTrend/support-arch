plugins {
    id("co.anitrend.arch")
}

dependencies {
    implementation(project(":extension"))
    implementation(project(":core"))
    implementation(project(":theme"))
    implementation(project(":domain"))
    implementation(project(":recycler"))

    implementation(libs.google.android.material)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.swipeRefreshLayout)
    implementation(libs.androidx.paging.runtime.ktx)
}