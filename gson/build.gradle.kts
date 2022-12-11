description = "Gson serialization support for typed2: ${rootProject.description}"

plugins {
  id("com.android.library")
  id("config-android-deploy")
}

android {
  namespace = "com.episode6.typed2.gson"
}

dependencies {
  api(project(":core"))
  implementation(libs.gson.core)
  implementation(libs.kotlinx.coroutines.core)

  testImplementation(libs.bundles.test.support)
}
