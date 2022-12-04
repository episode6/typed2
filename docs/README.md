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

With Typed2, we declare our keys in an object that subclasses a KeyNamespace. Each key namespace is specific to the type of object that key
can be used with.

SharedPreferences Example...

```kotlin
object PrefKeys : PrefKeyNamespace(prefix = "com.sample.prefkey.") {
  val MY_INT = key("someInt").int(default = 2)
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
  val MY_INT = key("someInt").int(default = 2)
  val MY_STRING = key("someString").string()
}

val bundle: Bundle = TODO()
val intent: Intent = TODO()

fun main() {
  // types are enforced by the keys
  val someInt: Int = bundle.get(Arguments.MY_INT)
  val someString: String? = intent.getExtra(Arguments.MY_STRING)

  bundle.set(Arguments.MY_INT, 23)
  intent.setExtra(Arguments.MY_STRING, "mj4l")
}
```

Can also be used to define screens for Navigation-Compose, enabling type-safe navigation arguments.

```kotlin
object MyScreen : NavScreen(name = "myScreen") {
  val MY_INT = key("someInt").int(default = 2)
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
  val someInt: Int = savedStateHandle.get(MyScreen.MY_INT)
  val someString: String? = savedStateHandle.get(MyScreen.MY_STRING)

  // type-safe navigation arguments
  navController.navigateTo(MyScreen) {
    set(MyScreen.MY_INT, 5)
    set(MyScreen.MY_STRING, "hi")
  }
}
```

## Object Serialization

We supply 3 modules to handle object serialization (they're very simple and it should be easy to build your own as well).

```kotlin
object PrefKeys : PrefKeyNamespace() {
  // with gson we can convert any data class to/from json using reflection
  val MY_GSON_OBJ = key("gsonObj").gson<SomeDataClass>()

  // with kotlinx-serialization-json we can convert classes annotated with @Serializable to/from json
  val MY_JSON_OBJ = key("jsonObj").json(default = SerialDataClass(), SerialDataClass::serializer)
}

// with kotlinx-serialization-bundlizer we can convert classes annotated with @Serializable to/from a Bundle (only applies to BundleKeyNamespace)
object Arguments : BundleKeyNamespace() {
  val MY_VIEW_STATE = key("viewState").bundlized(ViewState::serializer)
}
```

## Async Support

Typed2 is built with kotlin coroutines in mind. Any key can force its mapping onto a background thread using the `async()` function. When
using AsyncKeys, the `get()` and `set()` functions will be suspend functions.

```kotlin
object PrefKeys : PrefKeyNamespace() {
  // async() forces the gson execution into a coroutine run on Dispatchers.Default (by default)
  val MY_ASYNC_OBJ = key("asyncObj").gson<SomeBigDataClass>().async()
}

val sharedPreferences: SharedPreference = TODO()

fun main() {
  coroutineContext {
    val obj = sharedPreferences.get(MY_ASYNC_OBJ)
  }
}

```

## Properties and MutableStateFlows

All supported key-value stores also include extension methods to generate property delegates and MutableStateFlows

```kotlin
// getting and setting this var will call SharedPreferences.get() and set() under the hood 
var intPref: Int by sharedPreferences.property(PrefKeys.MY_INT)

// when using async key, properties will always be nullable and will always start as null
var jsonObj: SerialDataClass? by sharedPreferences.property(PrefKeys.MY_JSON_OBJ, viewModelScope)

// create a mutableStateFlow that writes new values back to sharedPreferences
val stringMutableStateFlow: MutableStateFlow<String> = sharedPreferences.mutableStateFlow(PrefKeys.MY_STRING, viewModelScope)

// when using async keys, mutableStateFlows will always be nullable and use null as an initial value
val jsonObjMutableStateFLow: MutableStateFlow<SerialDataClass?> = sharedPreferences.mutableStateFlow(PrefKeys.MY_JSON_OBJ, viewModelScope)
```
