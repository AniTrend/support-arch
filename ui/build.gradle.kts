import co.anitrend.arch.buildSrc.Libraries

plugins {
    id("co.anitrend.arch")
}

dependencies {
    implementation(project(":extension"))
    implementation(project(":core"))
    implementation(project(":theme"))
    implementation(project(":domain"))
    implementation(project(":recycler"))

    implementation(Libraries.Google.material)

    implementation(Libraries.AndroidX.Core.coreKtx)
    implementation(Libraries.AndroidX.SwipeRefresh.swipeRefreshLayout)
    implementation(Libraries.AndroidX.Paging.runtimeKtx)
}