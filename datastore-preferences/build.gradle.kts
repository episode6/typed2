description = "Jetpack DataStore (Preferences) support for typed2: ${rootProject.description}"

plugins {
  id("com.android.library")
  id("config-android-deploy")
}

android {
  namespace = "com.episode6.typed2.datastore.preferences"
  defaultConfig {
    minSdk = 23 // required by androidx.datastore 1.2+
  }
}

dependencies {
  api(project(":core"))
  api(libs.androidx.datastore.preferences.core)
  implementation(libs.kotlinx.coroutines.core)

  testImplementation(libs.bundles.test.support)
  testImplementation(libs.turbine.core)
  testImplementation(project(":gson"))
}
