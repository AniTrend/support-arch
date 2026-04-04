---
name: support-arch-build-dependencies
description: 'Understand and change support-arch build logic, module dependencies, version catalog entries, Dokka setup, KSP wiring, and shared Gradle conventions. Use for buildSrc edits, new dependencies, module graph changes, or documentation pipeline work.'
argument-hint: 'Describe the dependency, Gradle change, or build pipeline task you need to make'
---

# Support Arch Build And Dependencies

## What This Skill Produces

- A safe path for changing module dependencies or shared build logic.
- A map of where versions, plugins, Dokka, Spotless, and Android defaults are defined.
- Clear guidance on whether a change belongs in a module build file, the version catalog, or `buildSrc`.

## When To Use

- Adding or upgrading dependencies.
- Changing module relationships.
- Editing Dokka, Spotless, JDK, Android, publishing, or test conventions.
- Understanding how the annotation and processor modules are wired.

## Procedure

1. Read the [build map](./references/build-map.md) to find the owning file for the convention you want to change.
2. If the change introduces or upgrades a dependency, add the version and alias in `gradle/libs.versions.toml` first.
3. If the behavior should apply to many modules, implement it in `buildSrc` instead of duplicating it in several module build files.
4. Check the module graph before adding a project dependency so lower layers do not depend on higher UI layers.
5. Keep Dokka, Spotless, and test behavior aligned with the shared configuration.
6. When running Gradle locally, use the existing `jenv-gradle-low-ram` skill if Java selection or memory pressure becomes an issue.

## References

- [build map](./references/build-map.md)