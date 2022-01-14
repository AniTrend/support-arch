package co.anitrend.arch.buildSrc.common

internal object Versions {

    private fun Int.toVersion(): String {
        return if (this < 9) "0$this" else "$this"
    }

    const val compileSdk = 31
    const val targetSdk = 31
    const val minSdk = 21

    private const val major = 1
    private const val minor = 4
    private const val patch = 0
    private const val revision = 3

    private const val channel = "alpha"

    const val versionCode = major * 10_000_000 + minor * 10_000 + patch * 100 + revision * 1

    val versionName = if (revision > 0) 
        "$major.$minor.$patch-$channel${revision.toVersion()}"
    else "$major.$minor.$patch"

    const val mockk = "1.12.0"
    const val junit = "4.13.2"

    const val timber = "5.0.1"
    const val threeTenBp = "1.3.1"
    const val ktlint = "0.40.0"
}