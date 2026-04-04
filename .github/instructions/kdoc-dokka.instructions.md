---
description: Use when adding or changing public Kotlin APIs, KDoc, Dokka output, class docs, function docs, or property docs in support-arch modules.
applyTo: analytics/src/main/**/*.kt, annotation/src/main/**/*.kt, core/src/main/**/*.kt, data/src/main/**/*.kt, domain/src/main/**/*.kt, extension/src/main/**/*.kt, paging-legacy/src/main/**/*.kt, processor/src/main/**/*.kt, recycler/src/main/**/*.kt, recycler-paging-legacy/src/main/**/*.kt, request/src/main/**/*.kt, theme/src/main/**/*.kt, ui/src/main/**/*.kt
---

# KDoc And Dokka Guidance

- Treat KDoc as consumer documentation. The generated Dokka site is how downstream apps learn the library surface.
- Document every new or changed public or protected class, interface, object, enum, annotation, function, and property that a consumer may touch.
- Write documentation for someone outside this repo who does not already know the architecture. Explain what the API is for, when to use it, and which module or workflow it belongs to.
- For abstract base types, document the extension contract: what subclasses must override, invariants they must preserve, and when callbacks are invoked.
- For state and request types, document lifecycle, transitions, and where the state is expected to surface in recycler, UI, or request flows.
- For extension functions and extension properties, document the receiver, side effects, threading or lifecycle assumptions, and any important nullability or mutation behavior.
- For classes with important collaborators, link to nearby types with KDoc references instead of forcing consumers to search the repo manually.
- Preserve the existing house style when possible: a short summary first, then focused detail, with `@param`, `@property`, `@return`, `@throws`, `@see`, and `@since` where they add value.
- Do not invent version history. Only add `@since` when the version is already known or established in adjacent code.
- Avoid placeholder KDoc that only restates the type name. Explain behavior, expectations, and integration points.
- If behavior changes, update the docs in the same patch so the published site stays trustworthy.
- Packages under `.internal` are suppressed from Dokka. If an API is meant for library consumers, keep it in a documented public package.