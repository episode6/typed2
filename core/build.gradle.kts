plugins {
  id("com.android.library")
  id("config-android")
}

dependencies {
  testImplementation(libs.junit.core)
  testImplementation(libs.mockito.core)
  testImplementation(libs.mockito.kotlin)
  testImplementation(libs.assertk.core)
}
