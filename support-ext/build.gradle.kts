import co.anitrend.arch.buildSrc.Libraries

plugins {
    id("co.anitrend.arch")
}

dependencies {
    implementation(Libraries.Google.material)

    implementation(Libraries.AndroidX.Core.coreKtx)
    implementation(Libraries.AndroidX.StartUp.startUpRuntime)
    implementation(Libraries.AndroidX.Preference.preferenceKtx)

    implementation(Libraries.threeTenBp)
}