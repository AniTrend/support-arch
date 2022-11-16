package co.anitrend.arch.buildSrc.plugin.strategy

import co.anitrend.arch.buildSrc.plugin.extensions.library
import co.anitrend.arch.buildSrc.plugin.extensions.hasDependencies
import co.anitrend.arch.buildSrc.plugin.extensions.isUiModule
import co.anitrend.arch.buildSrc.plugin.extensions.implementation
import co.anitrend.arch.buildSrc.plugin.extensions.androidTest
import co.anitrend.arch.buildSrc.plugin.extensions.test
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler

internal class DependencyStrategy(private val project: Project) {

    private fun DependencyHandler.applyLoggingDependencies() {
        implementation(project.library("timber"))
    }

    private fun DependencyHandler.applyDefaultDependencies() {
        implementation(project.library("jetbrains-kotlin-stdlib-jdk8"))
        implementation(project.library("jetbrains-kotlin-reflect"))

        // Testing libraries
        test(project.library("junit"))
        test(project.library("mockk"))
        androidTest(project.library("mockk-android"))
    }

    private fun DependencyHandler.applyTestDependencies() {
        androidTest(project.library("androidx-test-core"))
        androidTest(project.library("androidx-test-coreKtx"))
        androidTest(project.library("androidx-test-runner"))
        androidTest(project.library("androidx-test-rules"))
    }

    private fun DependencyHandler.applyLifeCycleDependencies() {
        implementation(project.library("androidx-lifecycle-extensions"))
        implementation(project.library("androidx-lifecycle-runTimeKtx"))
        implementation(project.library("androidx-lifecycle-liveDataKtx"))
        implementation(project.library("androidx-lifecycle-viewModelKtx"))
    }

    private fun DependencyHandler.applyCoroutinesDependencies() {
        if (project.isUiModule())
            implementation(project.library("jetbrains-kotlinx-coroutines-android"))
        implementation(project.library("jetbrains-kotlinx-coroutines-core"))
        test(project.library("jetbrains-kotlinx-coroutines-test"))
        androidTest(project.library("cash-turbine"))
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