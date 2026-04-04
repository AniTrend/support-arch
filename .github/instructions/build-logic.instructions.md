---
description: Use when editing Gradle files, module dependencies, Dokka configuration, version catalog entries, GitHub workflows, or buildSrc logic in support-arch.
applyTo: build.gradle.kts, settings.gradle.kts, gradle/**/*.toml, buildSrc/**/*.kt, */build.gradle.kts, .github/workflows/*.yml
---

# Build Logic Guidance

- Prefer the shared `co.anitrend.arch` plugin and `buildSrc` helpers over duplicating Android, Kotlin, Dokka, Spotless, publishing, or test configuration in individual modules.
- The pinned Java and Kotlin toolchain is 21. Keep new build logic compatible with `.java-version` and the shared Android configuration.
- Add or update dependency versions in `gradle/libs.versions.toml` first, then reference the alias from modules or build logic.
- Keep module dependency changes aligned with the existing graph: lower layers should not depend on higher UI layers.
- `:annotation` is a JVM-only API module and `:processor` is the KSP implementation module. Most other modules are Android libraries.
- Shared Android defaults come from `buildSrc`, including SDK levels, view binding, JUnit Platform, compiler opt-ins, and packaging exclusions.
- Shared formatting comes from `ProjectSpotless.kt` and the license header file under `spotless/`.
- Shared documentation behavior comes from `ProjectDokka.kt`, the root Dokka task configuration, and `.github/workflows/gradle-dokka.yml`.
- If you need a new convention across many modules, prefer adding it once in `buildSrc` instead of repeating it in each `build.gradle.kts` file.
- When validating Gradle changes locally, pair the work with the existing `jenv-gradle-low-ram` skill if JDK alignment or memory pressure becomes a problem.