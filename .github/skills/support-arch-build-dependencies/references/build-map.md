# Build Map

Use this map to choose the right build file before editing.

| Concern | Primary files | Notes |
| --- | --- | --- |
| Module includes | `settings.gradle.kts` | Declares every published module in the repo |
| Root repositories and multi-module Dokka output | `build.gradle.kts` | Root `dokkaGenerate` writes the aggregate Dokka V2 site to `dokka-docs` |
| Shared plugin entry point | `buildSrc/.../plugin/CorePlugin.kt` | Applies Android or Kotlin plugin, Dokka, publishing, Spotless, sources, dependencies |
| Shared plugin application | `buildSrc/.../components/ProjectPlugins.kt` | Most modules use `co.anitrend.arch` |
| Shared Android defaults | `buildSrc/.../components/AndroidConfiguration.kt` | SDK levels, view binding, tests, compiler options, toolchain 21 |
| Shared dependency strategy | `buildSrc/.../strategy/DependencyStrategy.kt` | Default Kotlin, lifecycle, coroutines, logging, and test libraries |
| Shared Dokka behavior | `buildSrc/.../components/ProjectDokka.kt` | `reportUndocumented = true`, internal packages suppressed, Android docs linked |
| Shared formatting | `buildSrc/.../components/ProjectSpotless.kt`, `spotless/copyright.kt` | Ktlint and license header configuration |
| Dependency versions and aliases | `gradle/libs.versions.toml` | Add or update aliases here first |
| Annotation API | `annotation/build.gradle.kts`, `annotation/src/main/...` | JVM-only public annotation module |
| KSP processor wiring | `processor/build.gradle.kts`, `processor/src/main/...` | Depends on `annotation`, KotlinPoet, and KSP API |
| Dokka publication | `.github/workflows/gradle-dokka.yml` | Runs on `develop`, generates `dokka-docs`, deploys to `docs` branch |

## Module Dependency Snapshot

- `domain`: lowest Android module, no project dependencies.
- `request`: depends on `domain`, `extension`.
- `data`: depends on `domain`, `extension`, `request`.
- `core`: depends on `extension`, `data`, `domain`.
- `recycler`: depends on `extension`, `core`, `theme`, `domain`.
- `recycler-paging-legacy`: depends on `extension`, `recycler`.
- `ui`: depends on `extension`, `core`, `theme`, `domain`, `recycler`, `recycler-paging-legacy`.
- `paging-legacy`: depends on `data`, `domain`, `extension`, `request`.
- `annotation`: JVM module.
- `processor`: KSP module that depends on `annotation`.

## Edit Strategy

- New library version or alias: `libs.versions.toml`.
- Cross-module convention: `buildSrc`.
- One module only: that module's `build.gradle.kts`.
- Documentation generation or publish behavior: Dokka config plus workflow file.
- Annotation surface change: update both `annotation` and `processor` when needed.
