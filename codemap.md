# Repository Atlas: support-arch

## Project Responsibility

`support-arch` is a multi-module Android and Kotlin architecture library. It packages reusable domain state, request lifecycle helpers, repository-side data abstractions, Android platform utilities, RecyclerView infrastructure, screen scaffolding, theme assets, analytics contracts, and KSP code generation for downstream Android apps.

## System Entry Points

- `settings.gradle.kts`: Declares the published library modules: `analytics`, `annotation`, `core`, `extension`, `ui`, `data`, `domain`, `theme`, `recycler`, `processor`, `paging-legacy`, `request`, and `recycler-paging-legacy`.
- `build.gradle.kts`: Configures the root Dokka publication named `support-arch`, aggregates module documentation, and sets shared repositories.
- `buildSrc/`: Implements the shared `co.anitrend.arch` Gradle convention plugin used by most modules.
- `gradle/libs.versions.toml`: Centralizes Android, Kotlin, Dokka, KSP, WorkManager, Paging, RecyclerView, Material, and test dependency versions.
- `README.md`: Consumer-facing overview and Dokka module index.
- `AGENTS.md`: Repository operating guide for agents and maintainers.

## Design

The repository follows a layered Android library architecture:

1. Foundation modules define stable contracts and value types.
2. Request and data modules compose those contracts into lifecycle, state, retry, converter, mapper, and data-source abstractions.
3. Core, recycler, theme, and UI modules expose consumer-facing Android presentation infrastructure.
4. Annotation and processor modules form a compile-time code generation pair.
5. buildSrc keeps Gradle, Dokka, Spotless, source artifacts, dependencies, and publication behavior consistent across modules.

Primary patterns include sealed state models, interface contracts, abstract base classes, converter and mapper pipelines, callback-to-flow adapters, property delegates, RecyclerView adapter/controller separation, KSP provider and processor entry points, and Gradle convention plugin components.

## Data & Control Flow

Domain state starts in `domain` as `LoadState`, `NetworkState`, `RequestError`, and `UiState`. `request` wraps asynchronous work in request helpers, queues, wrappers, callbacks, listener dispatch, and status reports. `data` consumes request helpers and exposes repository-side load-state and UI-state contracts through sources, mappers, converters, transformers, and invalidation hooks.

Presentation modules consume those lower layers. `core` provides presenters, workers, providers, and shared model contracts. `recycler` turns item contracts and load states into adapter and view holder behavior. `paging-legacy` and `recycler-paging-legacy` bridge older AndroidX Paging 2 data sources and adapters. `ui` composes activities, fragments, list presenters, pagers, custom views, and state layout widgets. `theme` supplies shared resources and theme helpers used by UI consumers.

Compile-time generation follows a separate path: app code annotates navigation parameter classes with `@NavParam` from `annotation`, then `processor` resolves those symbols with KSP and writes Kotlin constants through KotlinPoet.

Build control flows through Gradle. Modules apply `co.anitrend.arch`, `buildSrc` configures Android or JVM behavior, shared dependency aliases come from `gradle/libs.versions.toml`, Spotless reads `spotless/copyright.kt`, and Dokka aggregates API documentation into `dokka-docs`.

## Integration

- Public documentation is generated with Dokka and published from `develop` to the `docs` branch.
- Most Android modules apply the shared `co.anitrend.arch` plugin and share dependency, formatting, source artifact, and publication rules.
- `annotation` is JVM-only and is consumed by `processor`.
- `processor` integrates KSP and KotlinPoet and is used at compile time by consumers that opt into annotation processing.
- Downstream Android apps consume modules independently according to the dependency layer they need.

## Directory Map

| Directory | Responsibility Summary | Detailed Map |
|-----------|------------------------|--------------|
| `analytics/` | Analytics and crash-reporting interface boundary for consumer-owned implementations. | [View Map](analytics/codemap.md) |
| `annotation/` | JVM annotation API for navigation parameter code generation. | [View Map](annotation/codemap.md) |
| `buildSrc/` | Shared Gradle convention plugin for Android, JVM, Dokka, Spotless, dependencies, sources, and publication defaults. | [View Map](buildSrc/codemap.md) |
| `core/` | Core Android presenters, workers, file provider helpers, and UI state contracts. | [View Map](core/codemap.md) |
| `data/` | Repository-side data abstractions connecting domain state, request helpers, converters, mappers, and data sources. | [View Map](data/codemap.md) |
| `domain/` | Low-level shared domain value types and UI state contracts. | [View Map](domain/codemap.md) |
| `extension/` | Android platform utilities for coroutines, lifecycle, connectivity, settings, preferences, dates, paging counters, and extension functions. | [View Map](extension/codemap.md) |
| `gradle/` | Shared Gradle version catalog, release metadata, wrapper settings, and daemon toolchain metadata. | [View Map](gradle/codemap.md) |
| `paging-legacy/` | AndroidX Paging 2 support for `PagedList`, `DataSource`, boundary callbacks, and Flow wrappers. | [View Map](paging-legacy/codemap.md) |
| `processor/` | KSP implementation for `@NavParam` that generates Kotlin navigation parameter constants. | [View Map](processor/codemap.md) |
| `recycler/` | RecyclerView adapters, holders, item contracts, load-state rows, selection helpers, and snap helpers. | [View Map](recycler/codemap.md) |
| `recycler-paging-legacy/` | Paging 2 `PagedListAdapter` bridge that reuses recycler contracts. | [View Map](recycler-paging-legacy/codemap.md) |
| `spotless/` | Shared Kotlin license header template consumed by the Spotless convention. | [View Map](spotless/codemap.md) |
| `theme/` | Reusable Material theme assets, resource overlays, drawables, dimensions, styles, and theme helpers. | [View Map](theme/codemap.md) |
| `ui/` | Activities, fragments, list presenters, pagers, custom views, state layouts, and widget extensions. | [View Map](ui/codemap.md) |
