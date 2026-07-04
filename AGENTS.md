# Agent Guidelines for typed2

## Validating the Project

Run checks and docs generation:

```bash
./gradlew check dokkaGenerateHtml
```

This command:
- Compiles all modules
- Runs all unit tests across every subproject
- Generates Dokka HTML documentation

A passing build requires exit code 0 with no test failures.

> **Note**: Android connected tests (`:sample-app:connectedCheck`) require a running Android emulator and are run in CI but not locally by default.

## Project Structure

This is an Android/JVM library providing type-safe keys for key-value stores. Key modules:

- `core/` — core type definitions and extension functions
- `datastore-preferences/` — Jetpack DataStore (Preferences) integration
- `saved-state-handle/` — SavedStateHandle integration
- `navigation-compose/` — Jetpack Navigation Compose integration
- `kotlinx-serialization-json/` — kotlinx.serialization JSON support
- `kotlinx-serialization-bundlizer/` — bundlizer serialization support
- `gson/` — Gson support
- `sample-app/` — Android sample application (not published)

## Build Configuration

- **Kotlin**: 2.3.21
- **JVM target**: 17
- **Gradle**: 9.5.1
- **Android**: compileSdk 35, minSdk 21
- **Docs**: Dokka 2.x

## Key Constraints

- **Explicit API**: Every public function, class, interface, and typealias must have an explicit `public` modifier
- **Compose Compiler**: Bundled with Kotlin 2.x via `org.jetbrains.kotlin.plugin.compose` — do not set `composeOptions.kotlinCompilerExtensionVersion`
- **kapt**: Still used for Hilt in the sample app; KSP migration is a future improvement

## Skills

See `.agents/` for available skills:
- `.agents/release-branch-skill/` — cut/create a new release branch
- `.agents/ship-release-skill/` — ship a release using `scripts/ship-release.py`
- `.agents/update-docs-skill/` — policy for keeping docs/CHANGELOG.md updated
