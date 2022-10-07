/*
 * Copyright (c) 2021 Geoffrey Hackett. All rights reserved.
 */

import org.gradle.api.JavaVersion

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
    static String[] compilerArgs = ["-Xcontext-receivers"]
  }
}
