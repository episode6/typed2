/*
 * Copyright (c) 2021 Geoffrey Hackett. All rights reserved.
 */

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPom

// I'd prefer to do this in kotlin but then its not accessible from our groovy plugins
class Config {
  class Jvm {
    static String name = "1.8"
    static JavaVersion targetCompat = JavaVersion.VERSION_1_8
    static JavaVersion sourceCompat = JavaVersion.VERSION_1_8
  }
  class Android {
    static int compileSdk = 33
    static int targetSdk = 33
    static int minSdk = 21
  }
  class Ktx {
    static String[] compilerArgs = []
  }
  class Site {
    static String generateJekyllConfig(Project project) {
      return """
        theme: jekyll-theme-cayman
        title: typed2
        description: ${project.rootProject.description}
        version: ${project.version}
        docsDir: https://episode6.github.io/typed2/docs/${if (Maven.isReleaseBuild(project)) "v${project.version}" else "main"}
        kotlinVersion: ${project.libs.versions.kotlin.core.get()}
        coroutineVersion: ${project.libs.versions.kotlinx.coroutines.get()}
""".stripIndent()
    }
  }
  class Maven {
    static void applyPomConfig(Project project, MavenPom pom) {
      pom.with {
        name = project.rootProject.name + "-" + project.name
        url = "https://github.com/episode6/typed2"
        licenses {
          license {
            name = "The MIT License (MIT)"
            url = "https://github.com/episode6/typed2/blob/main/LICENSE"
            distribution = "repo"
          }
        }
        developers {
          developer {
            id = "episode6"
            name = "episode6, Inc."
          }
        }
        scm {
          url = "extensible"
          connection = "scm:https://github.com/episode6/typed2.git"
          developerConnection = "scm:https://github.com/episode6/typed2.git"
        }
      }
      project.afterEvaluate {
        pom.description = project.description ?: project.rootProject.description
      }
    }

    public static boolean isReleaseBuild(Project project) {
      return project.version.contains("SNAPSHOT") == false
    }

    static String getRepoUrl(Project project) {
      if (isReleaseBuild(project)) {
        return "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
      } else {
        return "https://oss.sonatype.org/content/repositories/snapshots/"
      }
    }
  }
}
