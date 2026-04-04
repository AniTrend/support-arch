# Module Reference Map

Use this map to place code before searching for a specific file.

| Module | Depends on | Package roots | Use for | Dokka |
| --- | --- | --- | --- | --- |
| `analytics` | external only | `contract/` | analytics and crash-reporting contracts | `https://anitrend.github.io/support-arch/analytics/index.html` |
| `annotation` | none | root annotation package | public annotations such as `NavParam` | `https://anitrend.github.io/support-arch/annotation/index.html` |
| `core` | `extension`, `data`, `domain` | `model/`, `presenter/`, `provider/`, `worker/` | reusable app scaffolding that is not tied to specific screens | `https://anitrend.github.io/support-arch/core/index.html` |
| `data` | `domain`, `extension`, `request` | `common/`, `converter/`, `mapper/`, `source/`, `state/`, `transformer/` | converters, mappers, repository-side data sources, response handling | `https://anitrend.github.io/support-arch/data/index.html` |
| `domain` | none | `entities/`, `state/` | low-level shared models and state types such as `LoadState` | `https://anitrend.github.io/support-arch/domain/index.html` |
| `extension` | external only | `annotation/`, `coroutine/`, `dispatchers/`, `ext/`, `initializer/`, `lifecycle/`, `network/`, `preference/`, `settings/`, `util/` | cross-cutting utility code and Android or Kotlin helpers | `https://anitrend.github.io/support-arch/extension/index.html` |
| `paging-legacy` | `data`, `domain`, `extension`, `request` | `builder/`, `source/`, `util/` | legacy Paging 2 builders and sources | `https://anitrend.github.io/support-arch/paging-legacy/index.html` |
| `processor` | `annotation`, KSP | `codegen/`, `model/`, `provider/` | KSP implementation for generated code such as navigation args helpers | `https://anitrend.github.io/support-arch/processor/index.html` |
| `recycler` | `extension`, `core`, `theme`, `domain` | `action/`, `adapter/`, `common/`, `extensions/`, `helper/`, `holder/`, `model/`, `observer/`, `shared/`, `state/` | RecyclerView widgets, adapters, holders, load-state items, shared recycler behavior | `https://anitrend.github.io/support-arch/recycler/index.html` |
| `recycler-paging-legacy` | `extension`, `recycler` | `adapter/`, `extensions/` | bridge between recycler components and legacy paging | `https://anitrend.github.io/support-arch/recycler-paging-legacy/index.html` |
| `request` | `domain`, `extension` | `callback/`, `contract/`, `exception/`, `extension/`, `helper/`, `listener/`, `model/`, `queue/`, `report/`, `wrapper/` | request execution tracking, listener dispatch, reporting, wrappers, request helpers | `https://anitrend.github.io/support-arch/request/index.html` |
| `theme` | external only | `animator/`, `extensions/` | animations, theming, and visual behavior helpers | `https://anitrend.github.io/support-arch/theme/index.html` |
| `ui` | `extension`, `core`, `theme`, `domain`, `recycler`, `recycler-paging-legacy` | `activity/`, `common/`, `extension/`, `fragment/`, `pager/`, `view/` | base activities, fragments, widgets, pagers, and list presentation scaffolding | `https://anitrend.github.io/support-arch/ui/index.html` |

## Placement Heuristics

- State or value object reused across layers: start in `domain`.
- Request lifecycle, queueing, load-state propagation, or listeners: start in `request`.
- Conversion, mapping, transformation, or source abstraction: start in `data`.
- Generic Android helpers, preferences, coroutine helpers, network utilities, or extension functions: start in `extension`.
- Workers, presenters, providers, or base model helpers: start in `core`.
- RecyclerView adapters, holders, shared adapter state, or recycler widgets: start in `recycler`.
- Fragments, activities, pagers, or state-layout widgets: start in `ui`.
- Annotation API visible to consumers: `annotation`. KSP implementation details: `processor`.
- Legacy paging integrations should stay in `paging-legacy` or `recycler-paging-legacy` unless the task explicitly migrates away from those APIs.

## Consumer Notes

- Consumers usually touch `extension`, `domain`, `core`, `recycler`, `request`, and `ui` first.
- Favor documenting external extension points clearly because downstream apps often subclass or compose these base types instead of copying them.
- If a change affects a public type, assume the Dokka page is part of the deliverable.