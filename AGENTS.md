# support-arch Agent Guide

## Purpose

- `support-arch` is a multi-module Android and Kotlin architecture library.
- Optimize for reusable library abstractions, consumer ergonomics, and clear public documentation.
- The main downstream consumer today is `anitrend-v2`, so assume changes should be understandable to an external app team.

## High-Signal Repo Facts

- Public API docs are generated with Dokka and published to `https://anitrend.github.io/support-arch/`.
- CI publishes docs by running `./gradlew dokkaHtmlMultiModule` on `develop` and deploying `dokka-docs` to the `docs` branch.
- Most modules use the shared `co.anitrend.arch` Gradle plugin from `buildSrc`.
- Shared Android and Kotlin defaults live in `buildSrc`, not in individual module build files.
- The pinned Java version is `.java-version = 21.0.8` and the shared toolchain is Java 21.

## Module Groups

- Foundation: `extension`, `domain`, `request`, `data`
- UI scaffolding: `theme`, `core`, `recycler`, `recycler-paging-legacy`, `ui`
- Legacy paging: `paging-legacy`, `recycler-paging-legacy`
- Code generation: `annotation`, `processor`
- Integration: `analytics`

## Dependency Direction

- `domain` should remain low-level and broadly reusable.
- `request` builds request lifecycle and reporting on top of `domain` and `extension`.
- `data` adds converters, mappers, states, and sources on top of `domain`, `extension`, and `request`.
- `core` provides reusable workers, presenters, providers, and model helpers on top of `extension`, `data`, and `domain`.
- `recycler` and `ui` provide consumer-facing presentation infrastructure.
- `annotation` is the public annotation API and `processor` is the KSP implementation.
- Avoid adding upward dependencies into low-level modules.

## Placement Heuristics

- Shared state and value types: `domain`
- Request queueing, listener dispatch, wrappers, and reports: `request`
- Converters, mappers, data sources, and repository-side transformations: `data`
- Generic Android helpers and extension functions: `extension`
- Workers, presenters, providers, and reusable model helpers: `core`
- RecyclerView adapters, holders, shared adapter state, widgets, and load-state rows: `recycler`
- Fragments, activities, pagers, view widgets, and screen-scaffolding helpers: `ui`
- Public annotations: `annotation`; generated-code implementation: `processor`

## Documentation Contract

- Treat KDoc as product surface, not optional commentary.
- Dokka is configured with `reportUndocumented = true` for source sets.
- Document new or changed public and protected APIs in the same patch.
- Explain what the API does, when to use it, and what a consumer must provide, override, observe, or expect.
- Document lifecycle, threading, side effects, and state transitions when they matter.
- Packages matching `.internal` are suppressed from published docs, so consumer-facing APIs should not live there.

## Context Maintenance

- When major repository behavior changes, update the relevant `.github/instructions`, `.github/skills`, `AGENTS.md`, and consumer-facing docs in the same patch.
- Prefer replacing stale guidance over adding new guidance that contradicts it.
- New instructions or skills should be added only for recurring workflows or durable repo knowledge, not one-off tasks.

## Build Conventions

- Add dependency versions and aliases to `gradle/libs.versions.toml` first.
- Prefer changing shared behavior in `buildSrc` over duplicating configuration in several module build files.
- Shared formatting comes from Spotless and ktlint in `buildSrc`, with the header file under `spotless/`.
- Shared documentation behavior lives in `ProjectDokka.kt`, the root build script, and `.github/workflows/gradle-dokka.yml`.
- `annotation` is JVM-only. `processor` uses KSP and KotlinPoet. Most other modules are Android libraries.

## Validation

- Formatting: `./gradlew spotlessCheck` or `./gradlew spotlessApply`
- Docs: `./gradlew dokkaHtmlMultiModule`
- Tests: prefer targeted module tests before full-project runs
- If Gradle or Java alignment is unstable, use the existing `jenv-gradle-low-ram` skill

## Recommended Exploration Order

1. Start with `README.md` and the Dokka module page for the area you are touching.
2. Inspect the module build file and package roots.
3. Read `buildSrc` before changing shared build behavior.
4. Update KDoc whenever public behavior changes.