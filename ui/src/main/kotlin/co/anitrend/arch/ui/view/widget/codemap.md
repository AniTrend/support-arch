# ui/src/main/kotlin/co/anitrend/arch/ui/view/widget/

## Responsibility

Full screen state layout widget for loading, error, and content states.

## Design Patterns

ViewFlipper based state machine with coroutine flow collection. Configuration and load state are pushed through mutable flows.

## Data & Control Flow

On attach, it collects load state and config flows. Loading switches to loading child, error fills heading and message then switches to error child, idle or success switches back to content. Retry button emits `ClickableItem.State`.

## Integration Points

Used by list presenters for empty or initial screen states. Depends on domain load state, recycler click model, extension coroutine helpers, and ui state layout XML.
