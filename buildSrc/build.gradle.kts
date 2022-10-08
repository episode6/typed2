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
    create("ConfigureAndroidDeployablePlugin") {
      id = "config-android-deploy"
      implementationClass = "plugins.ConfigureAndroidDeployablePlugin"
    }
    create("ConfigureJvmDeployablePlugin") {
      id = "config-jvm-deploy"
      implementationClass = "plugins.ConfigureJvmDeployablePlugin"
    }
  }
}
