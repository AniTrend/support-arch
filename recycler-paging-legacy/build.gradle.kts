plugins {
    id("co.anitrend.arch")
}

android {
    namespace = "co.anitrend.arch.recycler.paging.legacy"
}

dependencies {
    implementation(project(":extension"))
    implementation(project(":recycler"))

    implementation(libs.androidx.paging.runtime.ktx)
    implementation(libs.androidx.recyclerView)
}
