import co.anitrend.arch.buildSrc.Libraries

plugins {
    id("co.anitrend.arch")
}

dependencies {
    implementation(project(":support-ext"))
    implementation(project(":support-data"))
    implementation(project(":support-domain"))

    implementation(Libraries.Google.material)
    implementation(Libraries.AndroidX.Work.runtimeKtx)
}