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

    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.swipeRefreshLayout)
    implementation(libs.androidx.paging.runtimeKtx)
}