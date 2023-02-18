plugins {
    id("co.anitrend.arch")
}

dependencies {
    implementation(project(":extension"))
    implementation(project(":core"))
    implementation(project(":theme"))
    implementation(project(":domain"))

    implementation(libs.google.android.material)

    implementation(libs.androidx.swipeRefreshLayout)
    implementation(libs.androidx.paging.runtime.ktx)
    implementation(libs.androidx.recyclerView)
}