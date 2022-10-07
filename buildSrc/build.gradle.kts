plugins {
  `java-gradle-plugin`
}

gradlePlugin {
  plugins {
    create("ConfigureJvmPlugin") {
      id = "config-jvm"
      implementationClass = "plugins.ConfigureJvmPlugin"
    }
    create("ConfigureAndroidPlugin") {
      id = "config-android"
      implementationClass = "plugins.ConfigureAndroidPlugin"
    }
  }
}
