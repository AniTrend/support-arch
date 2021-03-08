import co.anitrend.arch.buildSrc.Libraries

plugins {
    id("co.anitrend.arch")
}

dependencies {
    implementation(project(":support-ext"))
    implementation(project(":support-core"))
    implementation(project(":support-theme"))
    implementation(project(":support-domain"))
    implementation(project(":support-recycler"))

    implementation(Libraries.Google.material)

    implementation(Libraries.AndroidX.SwipeRefresh.swipeRefreshLayout)
    implementation(Libraries.AndroidX.Paging.runtimeKtx)
}