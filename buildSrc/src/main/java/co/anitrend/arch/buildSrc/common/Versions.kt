package co.anitrend.arch.buildSrc.common

internal object Versions {

    private fun Int.toVersion(): String {
        return if (this < 9) "0$this" else "$this"
    }

    const val compileSdk = 30
    const val targetSdk = 30
    const val minSdk = 21

    private const val major = 1
    private const val minor = 3
    private const val patch = 0
    private const val revision = 42
    private const val candidate = 1

    const val versionCode = major * 100_000 + minor * 10_000 + patch * 1_000 + revision * 100 + candidate * 10
    val versionName = "$major.$minor.$patch-rc${candidate.toVersion()}"

    const val mockk = "1.11.0"
    const val junit = "4.13.2"

    const val timber = "4.7.1"
    const val threeTenBp = "1.3.0"
    const val ktlint = "0.40.0"
}