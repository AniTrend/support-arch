plugins {
    id("co.anitrend.arch")
}

android {
    namespace = "co.anitrend.arch.recycler"
}

dependencies {
    implementation(project(":extension"))
    implementation(project(":core"))
    implementation(project(":theme"))
    implementation(project(":domain"))

    implementation(libs.google.android.material)

    implementation(libs.androidx.swipeRefreshLayout)
    implementation(libs.androidx.recyclerView)
}