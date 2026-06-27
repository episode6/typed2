package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

// plugin to configure defaults for simple java/kotlin libraries
class ConfigureJvmPlugin implements Plugin<Project> {

  @Override
  void apply(Project target) {
    target.with {
      plugins.apply("org.jetbrains.kotlin.jvm")

      kotlin {
        def jvmTargetClass = it.class.classLoader.loadClass("org.jetbrains.kotlin.gradle.dsl.JvmTarget")
        compilerOptions {
          jvmTarget.set(jvmTargetClass.fromTarget(Config.Jvm.name))
          freeCompilerArgs.add(Config.Kotlin.compilerArgs)
        }
      }

      java {
        sourceCompatibility = Config.Jvm.sourceCompat
        targetCompatibility = Config.Jvm.targetCompat
      }
    }
  }

}
