// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
  alias(libs.plugins.android.library) apply false
alias(libs.plugins.kotlin.serialization) apply false
  alias(libs.plugins.kotlin.compose) apply false
  alias(libs.plugins.hilt) apply false
  alias(libs.plugins.ksp) apply false
  alias(libs.plugins.dokka)
  id("config-site")
}

allprojects {
  group = "com.episode6.typed2"
  version = "1.9.9.1"
}
description = "Type-safe keys for obnoxious key-value stores."

tasks.wrapper {
  gradleVersion = libs.versions.gradle.core.get()
  distributionType = Wrapper.DistributionType.ALL
}
