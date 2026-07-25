# processor/src/main/kotlin/co/anitrend/arch/processor/provider/

## Responsibility

Contains `NavParamProcessorProvider`, the KSP provider that creates processor instances for the compiler environment.

## Design Patterns

Factory adapter from `SymbolProcessorEnvironment` to `NavParamProcessor` constructor arguments.

## Data & Control Flow

KSP calls `create`, the provider passes `codeGenerator`, `logger`, and `options` from the environment into a new processor instance.

## Integration Points

- Implements KSP `SymbolProcessorProvider`.
- Constructs `co.anitrend.arch.processor.NavParamProcessor`.
