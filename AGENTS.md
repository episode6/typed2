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
- **Android**: compileSdk 35, minSdk 21 (except `datastore-preferences` and `sample-app`, which require minSdk 23 to satisfy androidx.datastore 1.2+)
- **Docs**: Dokka 2.x

## Key Constraints

- **Explicit API**: Every public function, class, interface, and typealias must have an explicit `public` modifier
- **Compose Compiler**: Bundled with Kotlin 2.x via `org.jetbrains.kotlin.plugin.compose` — do not set `composeOptions.kotlinCompilerExtensionVersion`
- **kapt**: Still used for Hilt in the sample app; KSP migration is a future improvement

## Architecture Notes (non-obvious)

- **Key variance is the cross-store glue**: `Key`/`AsyncKey` are contravariant (`in`) on their GETTER/SETTER type
  params. Any key built against `PrimitiveKeyValueGetter`/`PrimitiveKeyValueSetter` (all core primitive builders and
  the gson/json serialization keys) is assignable to a store-specific key alias whose getter/setter interfaces extend
  the primitive ones. This is why one builder result works across stores without casts.
- **Creating keys outside `core`**: the `Key`/`AsyncKey` constructors are internal. Modules build keys through the
  public `NativeKeys.create(...)` factories plus the public combinators `mapType`/`defaultProvider`/`async(context)` —
  see `gson/` and `datastore-preferences/.../DataStoreKeys.kt` for the two patterns.
- **Builder shadowing pattern**: a store module may re-declare core's primitive builder signatures on its own builder
  interface (e.g. `DataStoreKeyBuilder.int(default)` returning an async key); use-site overload resolution picks the
  more-specific receiver automatically. Inside those functions you must upcast the receiver to `PrimitiveKeyBuilder`
  first (see the private `primitive()` helper in `DataStoreKeys.kt`) or the call recurses into itself.
- **`async(EmptyCoroutineContext)`** converts a sync key to an `AsyncKey` without a dispatcher hop — appropriate for
  trivial/identity mappers. Keep the default `async()` (`Dispatchers.Default`) for expensive serialization mappers.
- **`datastore-preferences` is async-only by design**: DataStore has no synchronous access, so the module's API accepts
  only `DataStoreKey`s and never uses `runBlocking`. The suspend boundary lives in the `DataStore<Preferences>`
  extensions; the (synchronous) key backers run against `Preferences` snapshots and `MutablePreferences` inside
  androidx's suspend `edit {}`. Store semantics: null writes remove the entry (DataStore cannot store null),
  `Preferences.Key` equality is name-based (so `contains`/`remove` work across value types), and `double()` stays
  string-backed to match typed2's other stores.
- **`DataStoreValue` wrapper**: DataStore mutableStateFlows can't be seeded with a real value, so they emit
  `DataStoreValue.Uninitialized` first and wrap reads in `DataStoreValue.Loaded` — `Loaded(null)` unambiguously means
  "key not present". Setting `Loaded(null)` removes the entry; setting `Uninitialized` is a no-op on the store.

## Testing Notes

- Unit tests use JUnit4 + assertk + mockito-kotlin (+ Turbine for flows) via `libs.bundles.test.support` — not JUnit5,
  not mockk.
- `datastore-preferences` tests run **real DataStores on plain JVM** (the `datastore-preferences-core` artifact is
  KMP) — no Robolectric needed. Create one per test:
  `PreferenceDataStoreFactory.create(scope = backgroundScope) { File(tmpFolder.root, "test.preferences_pb") }` with a
  JUnit `TemporaryFolder` rule (fresh file per test; the extension must be `.preferences_pb`).
- **Do not rely on `advanceUntilIdle()` to wait for DataStore work** under `runTest`: DataStore's file IO completes in
  real time, outside the virtual-time scheduler, so advance-then-assert races. Await observable state instead —
  `dataStore.flow(key).first { it == expected }`, `dataStore.data.first { predicate }`, or real-time polling via
  `withContext(Dispatchers.Default) { delay(...) }`.

## Adding a New Deployable Module

1. Create `<module>/build.gradle.kts` applying `com.android.library` + `config-android-deploy`, set a unique
   `android.namespace`, and add a `description` (it feeds the published POM).
2. Add the module to `include(...)` in `settings.gradle.kts`; add any new dependencies to `libs.versions.toml`.
3. Update the module list in this file, the Setup section + usage docs in `docs/README.md`, and `docs/CHANGELOG.md`
   (a docs change is required by the verify-docs CI gate whenever code changes).
4. Nothing else needs registering: Dokka aggregation and the publish workflow pick up every non-`sample-app` module
   automatically.
5. `./gradlew publishToMavenLocal` fails locally with "no configured signatory" unless GPG signing props are set —
   that's expected; CI supplies the signing key.

## Skills

See `.agents/` for available skills:
- `.agents/release-branch-skill/` — cut/create a new release branch
- `.agents/ship-release-skill/` — ship a release using `scripts/ship-release.py`
- `.agents/update-docs-skill/` — policy for keeping docs/CHANGELOG.md updated
