plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
include(
    ":analytics",
    ":annotation",
    ":core",
    ":extension",
    ":ui",
    ":data",
    ":domain",
    ":theme",
    ":recycler",
    ":processor",
    ":paging-legacy",
    ":request",
    ":recycler-paging-legacy",
)
