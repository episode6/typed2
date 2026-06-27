package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

class ConfigureAndroidComposePlugin implements Plugin<Project> {
  @Override
  void apply(Project target) {
    target.with {
      plugins.apply("org.jetbrains.kotlin.plugin.compose")
      android {
        buildFeatures {
          compose = true
        }
      }
    }
  }
}
