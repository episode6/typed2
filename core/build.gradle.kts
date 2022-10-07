plugins {
  id("com.android.library")
  id("config-android")
}

dependencies {
  testImplementation("junit:junit:4.13.2")
  testImplementation("org.mockito:mockito-core:4.8.0")
  testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
  testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.25")
}
