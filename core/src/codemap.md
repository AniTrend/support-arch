# core/src/

## Responsibility

Intermediate codemap for `core/src/`. The module only uses the main Android source set.

## Design Patterns

Directory pointer only. It keeps navigation explicit while the implementation details live in child package codemaps.

## Data & Control Flow

Control moves through this folder into `core/src/main/` and, when present, sibling resource codemaps.

## Integration Points

Use `core/src/main/` for the meaningful Kotlin package map and nearby `res/` codemaps for Android resources.
