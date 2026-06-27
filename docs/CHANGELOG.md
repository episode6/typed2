# ChangeLog

### v2.0.0-alpha04 - Unreleased


### v2.0.0-alpha03 - Released 06/27/2026

- Modernize build infrastructure: Kotlin 2.3.21, Gradle 9.5.1, Dokka 2.2.0, AGP 9.2.1, JVM target 17
- Update GitHub Actions: checkout@v6, setup-java@v5, gradle/actions/setup-gradle@v6, JDK 23
- Migrate Sonatype publishing to Central Portal
- Add agent skills: release-branch, ship-release, update-docs
- Add verify-docs CI workflow
- Split CI: build/check and connected checks on Ubuntu (with KVM-accelerated emulator)
- Replace deprecated `buildDir` with `layout.buildDirectory` throughout buildSrc
- Replace `tasks.create()` with `tasks.register()` throughout buildSrc
- Replace deprecated `kotlinOptions` DSL with `compilerOptions` in JVM and Android plugins
- Replace Compose compiler extension version with Kotlin Compose Compiler plugin
- Use Dokka 2.x javadoc jar for publishing instead of AGP's internal javadoc task
- Re-enable `explicitApi()`: add public visibility modifiers and explicit (incl. `Unit`) return types required by Kotlin 2.x strict mode
- Migrate sample-app Hilt from kapt to KSP and upgrade Hilt to 2.60
- Migrate tests to assertk 0.28.1 (`assertThat { }.isFailure()` → `assertFailure { }`)
- Fix Double serialization to use `BigDecimal(it)` instead of `toBigDecimal()`
- Migrate removed APIs: Material3 `SmallTopAppBar` → `TopAppBar`, Navigation Compose 2.8.x
- Upgrade dependency versions: kotlinx-coroutines 1.9.0, mockito 5.23.0, assertk 0.28.1, and others


### v2.0.0-alpha02 - Released 11/20/2022

 - Add `update(Key)` method to all supported Key/Value stores. 

### v2.0.0-alpha01 - Released 11/09/2022

- Complete kotlin re-imagining of Typed
