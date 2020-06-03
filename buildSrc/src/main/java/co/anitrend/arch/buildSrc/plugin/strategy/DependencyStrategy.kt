package co.anitrend.arch.buildSrc.plugin.strategy

import co.anitrend.arch.buildSrc.Libraries
import co.anitrend.arch.buildSrc.plugin.core
import co.anitrend.arch.buildSrc.plugin.data
import co.anitrend.arch.buildSrc.plugin.ext
import co.anitrend.arch.buildSrc.plugin.ui
import co.anitrend.arch.buildSrc.plugin.recycler
import org.gradle.api.artifacts.dsl.DependencyHandler

internal class DependencyStrategy(
    private val module: String
) {

    private fun DependencyHandler.applyLoggingDependencies() {
        add("implementation", Libraries.timber)
    }

    private fun DependencyHandler.applyDefaultDependencies() {
        add("implementation", Libraries.Kotlin.stdlib)

        // Testing libraries
        add("testImplementation", Libraries.junit)
        add("testImplementation", Libraries.mockk)
    }

    private fun DependencyHandler.applyTestDependencies() {
        add("androidTestImplementation", Libraries.AndroidX.Test.core)
        add("androidTestImplementation", Libraries.AndroidX.Test.rules)
        add("androidTestImplementation", Libraries.AndroidX.Test.runner)
    }

    private fun DependencyHandler.applyLifeCycleDependencies() {
        add("implementation", Libraries.AndroidX.Lifecycle.liveDataCoreKtx)
        add("implementation", Libraries.AndroidX.Lifecycle.runTimeKtx)
        add("implementation", Libraries.AndroidX.Lifecycle.liveDataKtx)
        add("implementation", Libraries.AndroidX.Lifecycle.extensions)
    }

    private fun DependencyHandler.applyCoroutinesDependencies() {
        if (module == ui)
            add("implementation", Libraries.Coroutines.android)
        add("implementation", Libraries.Coroutines.core)
        add("testImplementation", Libraries.Coroutines.test)
    }

    fun applyDependenciesOn(handler: DependencyHandler) {
        handler.applyDefaultDependencies()
        when (module) {
            core, data, ext, recycler, ui -> {
                handler.applyLifeCycleDependencies()
                handler.applyLoggingDependencies()
                handler.applyTestDependencies()
                handler.applyCoroutinesDependencies()
            }
        }
    }
}