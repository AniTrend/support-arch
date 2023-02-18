package co.anitrend.arch.buildSrc.common

object Configuration {

    private fun Int.toVersion(): String {
        return if (this < 9) "0$this" else "$this"
    }

    private const val major = 1
    private const val minor = 4
    private const val patch = 1
    private const val revision = 0

    private const val channel = "alpha"

    const val compileSdk = 33
    const val targetSdk = 33
    const val minSdk = 21

    /**
     * **RR**_X.Y.Z_
     * > **RR** reserved for build flavours and **X.Y.Z** follow the [versionName] convention
     */
    const val versionCode = major.times(1_000_000_000) +
            minor.times(1_000_000) +
            patch.times(1_000) +
            revision

    /**
     * Naming schema: X.Y.Z-variant##
     * > **X**(Major).**Y**(Minor).**Z**(Patch)
     */
    val versionName = if (revision > 0)
        "$major.$minor.$patch-$channel$${revision.toVersion()}"
    else
        "$major.$minor.$patch"
}
