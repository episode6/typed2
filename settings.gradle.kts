pluginManagement {
  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
  }
}
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
  }
  versionCatalogs {
    create("libs") { from(files("libs.versions.toml")) }
  }
}
rootProject.name = "typed2"
include(
  ":core",
  ":gson",
  ":kotlinx-serialization-bundlizer",
  ":kotlinx-serialization-json",
  ":navigation-compose",
  ":sample-app",
  ":saved-state-handle",
)
