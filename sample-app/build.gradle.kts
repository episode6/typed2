plugins {
  id("com.android.application")
  id("config-android")
  id("config-compose")
  kotlin("plugin.serialization")
}

android {
  defaultConfig {
    applicationId = "com.episode6.typed2.sampleapp"
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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


  androidTestImplementation(libs.androidx.test.ext.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.compose.ui.test.junit4)
}
