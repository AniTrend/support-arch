import co.anitrend.arch.buildSrc.Libraries

plugins {
    id("co.anitrend.arch")
}

dependencies {
    implementation(project(":support-ext"))
    implementation(project(":support-core"))
    implementation(project(":support-theme"))
    implementation(project(":support-domain"))

    implementation(Libraries.Google.material)

    implementation(Libraries.AndroidX.SwipeRefresh.swipeRefreshLayout)
    implementation(Libraries.AndroidX.Recycler.recyclerView)
    implementation(Libraries.AndroidX.Paging.runtimeKtx)
}