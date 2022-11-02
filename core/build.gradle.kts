description = "Core module of typed2: ${rootProject.description}"

plugins {
  id("com.android.library")
  id("config-android-deploy")
}

android {
  namespace = "com.episode6.typed2"
}

dependencies {
  implementation(libs.kotlinx.coroutines.core)
  implementation(libs.androidx.core.ktx)
  testImplementation(libs.bundles.test.support)
  testImplementation(libs.turbine.core)
}
