description = "SavedStateHandle support for typed2: ${rootProject.description}"

plugins {
  id("com.android.library")
  id("config-android-deploy")
  kotlin("plugin.serialization") // just for tests
}

android {
  namespace = "com.episode6.typed2.savedstatehandle"
}

dependencies {
  api(project(":core"))
  implementation(libs.androidx.livedata)
  implementation(libs.androidx.savedstatehandle)
  implementation(libs.kotlinx.coroutines.core)

  testImplementation(libs.bundles.test.support)
  testImplementation(libs.turbine.core)
  testImplementation(libs.androidx.testing.arch.core)
  testImplementation(project(":kotlinx-serialization-bundlizer"))
  implementation(libs.kotlinx.serialization.core)
  testImplementation(libs.robolectric.core)
}
