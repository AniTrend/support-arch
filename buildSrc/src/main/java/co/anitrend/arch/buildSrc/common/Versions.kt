package co.anitrend.arch.buildSrc.common

object Versions {
    const val compileSdk = 29
    const val targetSdk = 29
    const val minSdk = 21

    const val major = 1
    const val minor = 3
    const val patch = 0
    const val revision = 19

    const val versionCode = major * 100_000 + minor * 10_000 + patch * 1_000 + revision * 100
    const val versionName = "$major.$minor.$patch-beta$revision"

    const val mockk = "1.10.0"
    const val junit = "4.13"

    const val timber = "4.7.1"
    const val threeTenBp = "1.2.4"
}