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
  ":navigation-compose",
  ":saved-state-handle",
)
