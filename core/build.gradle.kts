import co.anitrend.arch.buildSrc.Libraries

plugins {
    id("co.anitrend.arch")
}

dependencies {
    implementation(project(":extension"))
    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(Libraries.Google.material)
    implementation(Libraries.AndroidX.Work.runtimeKtx)
}