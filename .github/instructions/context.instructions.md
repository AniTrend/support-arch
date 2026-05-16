---
applyTo: **
description: Use when understanding support-arch architecture, module boundaries, Dokka documentation, consumer-facing APIs, or shared Gradle/buildSrc behavior.
---

# Support Arch Context

- `support-arch` is a reusable Android and Kotlin architecture library, not an app. Favor reusable abstractions, extension points, and stable consumer-facing APIs over app-specific behavior.
- The main downstream consumer today is `anitrend-v2`, so changes should help an external app understand what to import, extend, override, observe, or wire together.
- Treat the published Dokka site as part of the product surface: `https://anitrend.github.io/support-arch/<module>/index.html`.

## Module Groups

- Foundation modules: `:extension`, `:domain`, `:request`, `:data`
- UI scaffolding modules: `:theme`, `:core`, `:recycler`, `:recycler-paging-legacy`, `:ui`
- Legacy paging modules: `:paging-legacy`, `:recycler-paging-legacy`
- Code generation modules: `:annotation`, `:processor`
- Integration module: `:analytics`

## Dependency Direction

- `:domain` is the lowest Android module and should remain stable, light, and broadly reusable.
- `:request` builds request tracking and reporting on top of `:domain` and `:extension`.
- `:data` adds converters, mappers, sources, and data-state helpers on top of `:domain`, `:extension`, and `:request`.
- `:core` provides reusable workers, presenters, providers, and model helpers on top of `:extension`, `:data`, and `:domain`.
- `:recycler` and `:ui` provide consumer-facing UI infrastructure and already depend on lower layers; prefer placing reusable presentation scaffolding there instead of pushing UI concerns downward.
- `:annotation` exposes public annotations, while `:processor` contains the KSP implementation. Keep annotation API changes coordinated with processor changes.
- Avoid introducing new dependency cycles. If a feature can live in a lower module, place it there instead of adding upward leakage.

## Package Expectations

- `:domain` mainly exposes `entities/` and `state/`.
- `:data` mainly exposes `common/`, `converter/`, `mapper/`, `source/`, `state/`, and `transformer/`.
- `:request` mainly exposes request helpers, listeners, models, wrappers, queues, and reports.
- `:core` mainly exposes reusable `model/`, `presenter/`, `provider/`, and `worker/` abstractions.
- `:recycler` mainly exposes adapters, holders, recycler widgets, load-state items, helpers, and extensions.
- `:ui` mainly exposes base activities, fragments, pagers, widgets, and presentation helpers.
- `:extension` is the cross-cutting utility module for coroutine, lifecycle, preference, network, and generic extension helpers.

## Build And Tooling Facts

- Most modules apply the shared `co.anitrend.arch` Gradle plugin from `buildSrc`.
- Shared Android defaults live in `buildSrc`, including `compileSdk = 35`, `minSdk = 23`, `targetSdk = 35`, view binding, JUnit Platform, Kotlin toolchain 21, and compiler opt-ins.
- The repo Java pin is `.java-version = 21.0.8`.
- Dependency versions belong in `gradle/libs.versions.toml` before they are referenced from module build files.
- Spotless and ktlint are enforced centrally via `buildSrc`, with the license header sourced from `spotless/copyright.kt`.

## Documentation Contract

- Dokka is configured centrally in `buildSrc` and the root build script; CI publishes `./gradlew dokkaGenerate` output from `dokka-docs` to the `docs` branch.
- Dokka has `reportUndocumented = true`, so undocumented public APIs are a quality problem, not an optional cleanup task.
- Packages matching `.internal` are intentionally suppressed from published docs. Do not hide consumer-facing APIs in internal packages.
- When changing public behavior, update KDoc in the same change. Document what the API does, when to use it, and what a consumer must provide or expect.

## Working Heuristics

- Put abstractions in the lowest module that can own them without depending on higher-level UI code.
- Preserve the existing legacy paging split unless the task explicitly migrates away from it.
- Prefer shared build logic changes in `buildSrc` over copy-pasting Gradle configuration into individual modules.
- When unsure where code belongs, start from the Dokka module page, inspect neighboring package roots, and confirm the dependency direction before editing.
