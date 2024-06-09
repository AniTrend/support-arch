package co.anitrend.arch.buildSrc.plugin.strategy

import co.anitrend.arch.buildSrc.plugin.extensions.hasDependencies
import co.anitrend.arch.buildSrc.plugin.extensions.isUiModule
import co.anitrend.arch.buildSrc.plugin.extensions.implementation
import co.anitrend.arch.buildSrc.plugin.extensions.androidTest
import co.anitrend.arch.buildSrc.plugin.extensions.isKotlinLibraryGroup
import co.anitrend.arch.buildSrc.plugin.extensions.test
import co.anitrend.arch.buildSrc.plugin.extensions.libs
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler

internal class DependencyStrategy(private val project: Project) {

    private fun DependencyHandler.applyLoggingDependencies() {
        implementation(project.libs.timber)
    }

    private fun DependencyHandler.applyDefaultDependencies() {
        implementation(project.libs.jetbrains.kotlin.stdlib.jdk8)
        implementation(project.libs.jetbrains.kotlin.reflect)

        // Testing libraries
        test(project.libs.mockk)
        test(project.libs.jetbrains.kotlin.test)
        if (!project.isKotlinLibraryGroup()) {
            androidTest(project.libs.mockk.android)
        }
    }

    private fun DependencyHandler.applyTestDependencies() {
        androidTest(project.libs.androidx.test.core.ktx)
        androidTest(project.libs.androidx.test.runner)
        androidTest(project.libs.androidx.test.rules)
    }

    private fun DependencyHandler.applyLifeCycleDependencies() {
        implementation(project.libs.androidx.lifecycle.extensions)
        implementation(project.libs.androidx.lifecycle.runTime.ktx)
        implementation(project.libs.androidx.lifecycle.liveData.ktx)
    }

    private fun DependencyHandler.applyCoroutinesDependencies() {
        if (project.isUiModule())
            implementation(project.libs.jetbrains.kotlinx.coroutines.android)
        implementation(project.libs.jetbrains.kotlinx.coroutines.core)
        test(project.libs.jetbrains.kotlinx.coroutines.test)
        test(project.libs.cash.turbine)
    }

    fun applyDependenciesOn(handler: DependencyHandler) {
        handler.applyDefaultDependencies()
        if (project.hasDependencies()) {
            handler.applyLifeCycleDependencies()
            handler.applyLoggingDependencies()
            handler.applyTestDependencies()
            handler.applyCoroutinesDependencies()
        }
    }
}