package co.anitrend.arch.buildSrc.plugin.strategy

import co.anitrend.arch.buildSrc.Libraries
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

    companion object {
        private const val core = "support-core"
        private const val data = "support-data"
        private const val domain = "support-domain"
        private const val ext = "support-ext"
        private const val recycler = "support-recycler"
        private const val theme = "support-theme"
        private const val ui = "support-ui"
    }
}