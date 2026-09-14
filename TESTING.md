# Testing

## Current testing strategy

The project includes:

- unit tests for the deterministic intervention engine
- Room/data reset tests for local persistence cleanup
- Compose UI smoke tests for key screens and settings flow
- Android lint checks for code quality
- Detekt static analysis when Gradle tasks are invoked

## How to run locally

```bash
./gradlew test
./gradlew lint
./gradlew detekt
./gradlew bundleRelease
```

## Scope

The existing test suite is focused on the product behavior that is already implemented: local logic, persistence, and app-screen behavior. It does not attempt to validate live OS-level accessibility flows beyond what the environment can exercise.

## Release validation

Release validation should include:

- successful compilation
- unit tests passing
- lint passing
- release bundle generation succeeding
- no secret or credential material committed to source control
