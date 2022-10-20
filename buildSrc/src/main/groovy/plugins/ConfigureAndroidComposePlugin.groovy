package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

class ConfigureAndroidComposePlugin implements Plugin<Project> {
  @Override
  void apply(Project target) {
    target.with {
      android {
        buildFeatures {
          compose = true
        }

        composeOptions {
          kotlinCompilerExtensionVersion = libs.versions.compose.core.get()
        }
      }
    }
  }
}
