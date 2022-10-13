description = "SavedStateHandle support for typed2: ${rootProject.description}"

plugins {
  id("com.android.library")
  id("config-android-deploy")
}

android {
  namespace = "com.episode6.typed2.savedstatehandle"
}

dependencies {
  api(project(":core"))
  implementation(libs.savedstatehandle)
  implementation(libs.kotlinx.coroutines.core)
  testImplementation(libs.bundles.test.support)
}
