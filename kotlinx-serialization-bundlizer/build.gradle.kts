description = "Kotlinx bundle serialization support for typed2: ${rootProject.description}"

plugins {
  id("com.android.library")
  id("config-android-deploy")
  kotlin("plugin.serialization") // just for tests
}

android {
  namespace = "com.episode6.typed2.kotlinx.serialization.bundlizer"
}

dependencies {
  api(project(":core"))
  implementation(libs.kotlinx.serialization.core)
  implementation(libs.kotlinx.serialization.bundlizer)

  testImplementation(libs.bundles.test.support)
  testImplementation(libs.robolectric.core)
}
