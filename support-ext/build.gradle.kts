import co.anitrend.arch.buildSrc.Libraries

plugins {
    id("co.anitrend.arch")
}

dependencies {
    implementation(Libraries.Google.material)

    implementation(Libraries.AndroidX.coreKtx)
    implementation(Libraries.AndroidX.preference)

    implementation(Libraries.threeTenBp)
}