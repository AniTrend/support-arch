plugins {
    id("co.anitrend.arch")
    alias(libs.plugins.google.devtools.ksp)
}

dependencies {
    implementation(project(":annotation"))
    implementation(libs.squareup.kotlinpoet)
    implementation(libs.google.devtools.ksp.api)

    testImplementation(kotlin("test"))
    testImplementation(libs.kotlin.compile.testing.ksp)
}

tasks.test {
    useJUnitPlatform()
}