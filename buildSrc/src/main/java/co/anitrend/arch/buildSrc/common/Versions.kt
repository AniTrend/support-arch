package co.anitrend.arch.buildSrc.common

internal object Versions {
    const val compileSdk = 30
    const val targetSdk = 30
    const val minSdk = 21

    private const val major = 1
    private const val minor = 3
    private const val patch = 0
    private const val revision = 38

    const val versionCode = major * 100_000 + minor * 10_000 + patch * 1_000 + revision * 100
    const val versionName = "$major.$minor.$patch-beta$revision"

    const val mockk = "1.10.5"
    const val junit = "4.13.1"

    const val timber = "4.7.1"
    const val threeTenBp = "1.3.0"
}