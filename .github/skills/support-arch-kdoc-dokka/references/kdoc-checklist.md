# KDoc Checklist

Use these prompts when documenting public APIs.

## Class Or Interface

- What problem does this type solve?
- Which module or workflow is it part of?
- Should consumers instantiate it, subclass it, or only observe it?
- What collaborators or neighboring types matter?
- What lifecycle, threading, or state assumptions matter?

Template:

```kotlin
/**
 * Short summary of the type and the workflow it belongs to.
 *
 * Explain when consumers should use, implement, or extend it.
 * Mention important collaborators with KDoc links.
 *
 * @property ...
 * @since ...
 */
```

## Function

- What does it do for the caller?
- When should it be called?
- What are the side effects, threading assumptions, or lifecycle requirements?
- What does it return or publish?
- What can fail and how does failure surface?

Template:

```kotlin
/**
 * Short summary of the behavior.
 *
 * Add timing, state, or lifecycle detail when it matters.
 *
 * @param ...
 * @return ...
 * @throws ...
 */
```

## Property

- Is this configuration, state, or a contract consumers must provide?
- When is it read or updated?
- Is it safe to mutate directly, or should callers use another API?

Template:

```kotlin
/**
 * Explains what this property represents and when consumers should read or set it.
 */
```

## Extension Function Or Property

- Document the receiver explicitly.
- Explain hidden dependencies such as context, lifecycle owner, coroutine scope, or thread.
- Call out side effects and mutations.

## Repo-Specific Reminders

- Dokka reports undocumented public APIs, so documentation is not optional for consumer-facing surfaces.
- `.internal` packages are suppressed from published docs.
- Use `@since` only when the version is known from existing code or release context.
- If the behavior changed, update the KDoc in the same patch.