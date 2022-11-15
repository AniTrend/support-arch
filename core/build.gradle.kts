plugins {
    id("co.anitrend.arch")
}

dependencies {
    implementation(project(":extension"))
    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(libs.google.android.material)
    implementation(libs.androidx.work.runtimeKtx)
}