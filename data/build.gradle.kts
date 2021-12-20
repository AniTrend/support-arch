import co.anitrend.arch.buildSrc.Libraries

plugins {
    id("co.anitrend.arch")
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":extension"))

    implementation(Libraries.AndroidX.Paging.common)
    implementation(Libraries.AndroidX.Paging.runtime)
    implementation(Libraries.AndroidX.Paging.runtimeKtx)
}