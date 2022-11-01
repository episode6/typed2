// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
  id("com.android.library") version(libs.versions.android.gradle.get()) apply(false)
  kotlin("android") version(libs.versions.kotlin.core.get()) apply(false)
  id("org.jetbrains.dokka") version (libs.versions.dokka.core.get())
  kotlin("plugin.serialization") version(libs.versions.kotlin.core.get()) apply(false)
  id("com.google.dagger.hilt.android") version(libs.versions.hilt.core.get()) apply(false)
}

allprojects {
  group = "com.episode6.typed2"
  version = "2.0.0-SNAPSHOT"
}
description = "Type-safe keys for obnoxious key-value stores."

tasks.wrapper {
  gradleVersion = libs.versions.gradle.core.get()
  distributionType = Wrapper.DistributionType.ALL
}
