description = ""

plugins {
  id("com.android.library")
  id("config-android-deploy")
}

android {
  namespace = "com.episode6.typed2.navigation.compose"
}

dependencies {
  api(project(":core"))
  implementation(libs.androidx.nav.compose)

  testImplementation(libs.bundles.test.support)
  testImplementation(project(":saved-state-handle"))
  testImplementation(libs.androidx.savedstatehandle)
}
