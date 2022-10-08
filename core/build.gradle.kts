plugins {
  id("com.android.library")
  id("config-android")
}

android {
  namespace = "com.episode6.typed2"
}

dependencies {
  testImplementation(libs.junit.core)
  testImplementation(libs.mockito.core)
  testImplementation(libs.mockito.kotlin)
  testImplementation(libs.assertk.core)
}
