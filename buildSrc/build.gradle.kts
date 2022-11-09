plugins {
  `java-gradle-plugin`
}

gradlePlugin {
  plugins {
    create("ConfigureSitePlugin") {
      id = "config-site"
      implementationClass = "plugins.ConfigSitePlugin"
    }
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
    create("ConfigureAndroidComposePlugin") {
      id = "config-compose"
      implementationClass = "plugins.ConfigureAndroidComposePlugin"
    }
  }
}
