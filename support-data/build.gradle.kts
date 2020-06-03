import co.anitrend.arch.buildSrc.Libraries

plugins {
    id("co.anitrend.arch")
}

dependencies {
    implementation(project(":support-domain"))
    implementation(project(":support-ext"))

    implementation(Libraries.AndroidX.Paging.common)
    implementation(Libraries.AndroidX.Paging.runtime)
    implementation(Libraries.AndroidX.Paging.runtimeKtx)
}