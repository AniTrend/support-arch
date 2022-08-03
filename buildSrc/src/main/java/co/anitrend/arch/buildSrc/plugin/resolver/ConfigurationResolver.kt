package co.anitrend.arch.buildSrc.plugin.resolver

import com.github.benmanes.gradle.versions.updates.resolutionstrategy.ComponentSelectionWithCurrent

fun ComponentSelectionWithCurrent.handleDependencySelection() {
    val reject = listOf("preview", "m", "rc", "alpha", "beta")
        .map { qualifier ->
            val pattern = "(?i).*[.-]$qualifier[.\\d-]*"
            Regex(pattern, RegexOption.IGNORE_CASE)
        }
        .any { it.matches(candidate.version) }
    if (reject)
        reject("$candidate version does not fit acceptance criteria")
}