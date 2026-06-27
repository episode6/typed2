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

        compilerOptions {
          def jvmTargetClass = rootProject.class.classLoader.loadClass("org.jetbrains.kotlin.gradle.dsl.JvmTarget")
          jvmTarget = jvmTargetClass.fromTarget(Config.Jvm.name)
          freeCompilerArgs.add(Config.Kotlin.compilerArgs)
        }

        packaging {
          resources {
            excludes += '/META-INF/{AL2.0,LGPL2.1}'
          }
        }
      }
    }
  }
}
