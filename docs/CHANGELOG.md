# ChangeLog

### v2.0.0-alpha03 - Unreleased

- Modernize build infrastructure: Kotlin 2.3.21, Gradle 9.5.1, Dokka 2.2.0, AGP 8.9.1, JVM target 17
- Update GitHub Actions: checkout@v6, setup-java@v5, gradle/actions/setup-gradle@v6, JDK 23
- Migrate Sonatype publishing to Central Portal
- Add agent skills: release-branch, ship-release, update-docs
- Add verify-docs CI workflow
- Replace deprecated `buildDir` with `layout.buildDirectory` throughout buildSrc
- Replace `tasks.create()` with `tasks.register()` throughout buildSrc
- Replace deprecated `kotlinOptions` DSL with `compilerOptions` in JVM plugin
- Replace Compose compiler extension version with Kotlin Compose Compiler plugin
- Upgrade dependency versions: kotlinx-coroutines 1.9.0, mockito 4.11.0, assertk 0.28.1, and others


### v2.0.0-alpha02 - Released 11/20/2022

 - Add `update(Key)` method to all supported Key/Value stores. 

### v2.0.0-alpha01 - Released 11/09/2022

- Complete kotlin re-imagining of Typed
