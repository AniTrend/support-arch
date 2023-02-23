plugins {
    id("co.anitrend.arch")
}

android {
    namespace = "co.anitrend.arch.extension"
}

dependencies {
    implementation(libs.google.android.material)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.startupRuntime)
    implementation(libs.androidx.preference.ktx)

    implementation(libs.threeTenBp)
}