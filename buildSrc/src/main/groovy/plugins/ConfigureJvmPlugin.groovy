package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

// plugin to configure defaults for simple java/kotlin libraries
class ConfigureJvmPlugin implements Plugin<Project> {

  @Override
  void apply(Project target) {
    target.with {
      plugins.apply("org.jetbrains.kotlin.jvm")
      java {
        sourceCompatibility = Config.Jvm.sourceCompat
        targetCompatibility = Config.Jvm.targetCompat
      }
      compileKotlin {
        kotlinOptions {
          jvmTarget = Config.Jvm.name
          freeCompilerArgs = Config.Ktx.compilerArgs
        }
      }
      // i know there has to be a less repetitive way to do this for all tasks of type KotlinCompile, but
      // that wasn't working for me in this case (i think because the kotlin plugins are not actually on the
      // classpath of buildSrc
      compileTestKotlin {
        kotlinOptions {
          jvmTarget = Config.Jvm.name
          freeCompilerArgs = Config.Ktx.compilerArgs
        }
      }
    }
  }

}
