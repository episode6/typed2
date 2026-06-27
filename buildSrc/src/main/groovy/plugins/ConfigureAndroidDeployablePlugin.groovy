package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication

class ConfigureAndroidDeployablePlugin implements Plugin<Project> {
  @Override
  void apply(Project target) {
    target.with {
      plugins.with {
        apply(ConfigureAndroidPlugin)
        apply(CommonDeployablePlugin)
      }

      android {
        publishing {
          singleVariant("release") {
            withSourcesJar()
            // Intentionally NOT withJavadocJar(): AGP's internal javadoc task uses a
            // legacy bundled Dokka whose ASM can't read Kotlin 2.x sealed-class bytecode
            // (PermittedSubclasses requires ASM9). We attach our own Dokka 2.x javadoc
            // jar (the `javadocJar` task from CommonDeployablePlugin) instead.
          }
        }
      }

      afterEvaluate {
        publishing {
          publications {
            release(MavenPublication) {
              from components.release
              artifact(tasks.named("javadocJar"))
              Config.Maven.applyPomConfig(target, pom)
            }
          }
        }
      }
    }
  }
}
