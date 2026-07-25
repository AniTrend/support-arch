# paging-legacy/src/main/kotlin/co/anitrend/arch/paging/

## Responsibility

Intermediate codemap for `paging-legacy/src/main/kotlin/co/anitrend/arch/paging/`. Intermediate paging namespace. Legacy Paging 2 APIs are under the `legacy` child package.

## Design Patterns

Directory pointer only. It keeps navigation explicit while the implementation details live in child package codemaps.

## Data & Control Flow

Control moves through this folder into `paging-legacy/src/main/kotlin/co/anitrend/arch/paging/legacy/` and, when present, sibling resource codemaps.

## Integration Points

Use `paging-legacy/src/main/kotlin/co/anitrend/arch/paging/legacy/` for the meaningful Kotlin package map and nearby `res/` codemaps for Android resources.
