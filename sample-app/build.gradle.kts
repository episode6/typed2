plugins {
  id("com.android.application")
  id("config-android")
  id("config-compose")
  kotlin("kapt")
  kotlin("plugin.serialization")
  id("com.google.dagger.hilt.android")
  id("kotlin-parcelize")
}

android {
  namespace = "com.episode6.typed2.sampleapp"
  defaultConfig {
    applicationId = "com.episode6.typed2.sampleapp"
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "com.episode6.typed2.sampleapp.HiltTestRunner"
  }

  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
}

dependencies {
  implementation(libs.androidx.core.ktx)
  implementation(libs.compose.ui.core)
  implementation(libs.compose.material3.core)
  implementation(libs.androidx.lifecycle.runtime)
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.nav.compose)
  implementation(libs.kotlinx.serialization.core)

  implementation(project(":core"))
  implementation(project(":gson"))
  implementation(project(":kotlinx-serialization-bundlizer"))
  implementation(project(":kotlinx-serialization-json"))
  implementation(project(":navigation-compose"))
  implementation(project(":saved-state-handle"))

  implementation(libs.hilt.android.core)
  implementation(libs.hilt.nav.compose)
  kapt(libs.hilt.compiler)
  kaptAndroidTest(libs.hilt.compiler)

  androidTestImplementation(libs.androidx.test.ext.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.compose.ui.test.junit4)
  androidTestImplementation(libs.hilt.android.testing)
  androidTestImplementation(libs.assertk.core)
  androidTestImplementation(libs.turbine.core)
  androidTestImplementation(libs.kotlinx.coroutines.test)
  androidTestImplementation(libs.androidx.livedata)
  androidTestImplementation(libs.androidx.testing.arch.core)
}
