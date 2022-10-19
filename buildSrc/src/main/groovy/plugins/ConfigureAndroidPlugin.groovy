package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

// plugin to configure defaults for android libraries and apps.
// i'd prefer to do this in kotlin but unsure if there's a way to
// w/o direct access the internals of the android plugin.
class ConfigureAndroidPlugin implements Plugin<Project> {

  @Override
  void apply(Project target) {
    target.with {
      plugins.apply("org.jetbrains.kotlin.android")
      android {
        compileSdk Config.Android.compileSdk

        defaultConfig {
          minSdk Config.Android.minSdk
          targetSdk Config.Android.targetSdk
          vectorDrawables {
            useSupportLibrary true
          }
        }

        compileOptions {
          sourceCompatibility Config.Jvm.sourceCompat
          targetCompatibility Config.Jvm.targetCompat
        }

        kotlinOptions {
          jvmTarget = Config.Jvm.name
          freeCompilerArgs = Config.Ktx.compilerArgs
        }

        packagingOptions {
          resources {
            excludes += '/META-INF/{AL2.0,LGPL2.1}'
          }
        }
      }
    }
  }
}
