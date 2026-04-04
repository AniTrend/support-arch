---
name: support-arch-kdoc-dokka
description: 'Write or improve KDoc for public APIs in support-arch. Use for Dokka updates, class docs, function docs, property docs, consumer-facing documentation, and explaining how downstream apps should use or extend library APIs.'
argument-hint: 'Describe the public API, module, or documentation gap you need to cover'
---

# Support Arch KDoc And Dokka

## What This Skill Produces

- Consumer-facing KDoc that reads well on the published Dokka site.
- Documentation that explains extension contracts, lifecycle expectations, and module context.
- A repeatable checklist for updating docs whenever public behavior changes.

## When To Use

- Adding or changing a public class, interface, annotation, function, property, or enum.
- Explaining how a downstream app should implement or extend a support-arch abstraction.
- Tightening documentation before a release or after a behavior change.

## Procedure

1. Identify the public or protected surface that changed.
2. Read the [KDoc checklist](./references/kdoc-checklist.md) and match the API shape to the closest template.
3. Document what the API does, when to use it, and what a consumer is expected to provide or observe.
4. Add lifecycle, threading, side effects, state transitions, or override expectations when they matter.
5. Link adjacent types with KDoc references so Dokka helps consumers move through the API surface.
6. If the type belongs to a new package area, consider whether nearby package or module docs also need an update.

## Quality Bar

- Summary first, details second.
- Avoid tautologies such as repeating the type name without explaining behavior.
- Keep docs aligned with real behavior in the code, not the intended behavior from an older implementation.

## References

- [KDoc checklist](./references/kdoc-checklist.md)