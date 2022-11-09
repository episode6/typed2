A kotlin re-imagining of [Typed! for Android](https://github.com/episode6/typed)

[![Maven Central](https://img.shields.io/maven-central/v/com.episode6.typed2/core.svg?style=flat-square)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.episode6.typed2%22)

## The premise:

"Fix" Android's obnoxious Key-Value stores by defining Keys that have all info necessary to get/set values including a return Type, a
defaultValue (if any) and any serialization/deserialization instructions.

## Setup

```groovy
def typed2Version = "{{ site.version }}"
dependencies {
  // core implementation: supports SharedPreferences, Intents, Bundles & PersistableBundles
  implementation "com.episode6.typed2:core:$typed2Version"

  // optional add-on modules
  implementation "com.episode6.typed2:saved-state-handle:$typed2Version"
  implementation "com.episode6.typed2:navigation-compose:$typed2Version"

  // optional serialization support
  implementation "com.episode6.typed2:gson:$typed2Version"
  implementation "com.episode6.typed2:kotlinx-serialization-json:$typed2Version"
  implementation "com.episode6.typed2:kotlinx-serialization-bundlizer:$typed2Version"
}
```

<sup>Typed2 v{{ site.version }} is compiled against Kotlin v{{ site.kotlinVersion }} and Coroutines v{{ site.coroutineVersion }}</sup>

## Usage

```kotlin
object PrefKeys : PrefKeyNamespace(prefix = "com.sample.prefkey.") {
  val MY_INT    = key("someInt").int(default = 2)
  val MY_STRING = key("someString").string() // no default means null is the default
}

val sharedPreferences: SharedPreference = TODO()

fun main() {
  // types & nullability are enforced by the keys
  val someInt = sharedPreferences.get(PrefKeys.MY_INT)
  val someString = sharedPreferences.get(PrefKeys.MY_STRING)
  
  sharedPreferences.edit { 
    set(PrefKeys.MY_INT, 42)
    set(PrefKeys.MY_STRING, "answer")
  }
}
```

Also works with Bundles...

```kotlin
object Arguments : BundleKeyNamespace(prefix = "com.sample.arguments.") {
  val MY_INT    = key("someInt").int(default = 2)
  val MY_STRING = key("someString").string()
}

val bundle: Bundle = TODO()

fun main() {
  // types are enforced by the keys
  val someInt: Int = bundle.get(Arguments.MY_INT)
  val someString: String? = bundle.get(Arguments.MY_STRING)
  
  bundle.set(Arguments.MY_INT, 23)
  bundle.set(Arguments.MY_STRING, "mj4l")
}
```

Can also be used to define screens for Navigation-Compose, enabling type-safe navigation arguments.

```kotlin
object MyScreen : NavScreen(name = "myScreen") {
  val MY_INT    = key("someInt").int(default = 2)
  val MY_STRING = key("someString").string()
}

val savedStateHandle: SavedStateHandle = TODO()
val navController: NavController = TODO()

@Composable fun MyNavigationDefinition(navController: NavHostController) {
  NavHost(navController = navController, startScreen = MyScreen) { // note: startScreen must not have any required args
    
    // automatically define the route based on MyScreen's arguments
    composableScreen(MyScreen) { 
      /* actual composable UI */
    }
  }
}

fun main() {
  // can pull nav arguments from either SavedStateHandles or Bundles
  val someInt: Int = savedStateHandle.get(Arguments.MY_INT)
  val someString: String? = savedStateHandle.get(Arguments.MY_STRING)

  // type-safe navigation arguments
  navController.navigateTo(MyScreen) {
    set(MyScreen.MY_INT, 5)
    set(MyScreen.MY_STRING, "hi")
  }
}
```
