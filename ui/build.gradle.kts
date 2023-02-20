plugins {
    id("co.anitrend.arch")
}

android {
    namespace = "co.anitrend.arch.ui"
}

dependencies {
    implementation(project(":extension"))
    implementation(project(":core"))
    implementation(project(":theme"))
    implementation(project(":domain"))
    implementation(project(":recycler"))
    implementation(project(":recycler-paging-legacy"))

    implementation(libs.google.android.material)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.swipeRefreshLayout)
    implementation(libs.androidx.paging.runtime.ktx)
}