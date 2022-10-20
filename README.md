PROTOTYPE: typed2 for android (sequel to [Typed! for Android](https://github.com/episode6/typed))

I'm open sourcing this because I've been talking about it with some folks, but this is not ready for use.

The premise: "Fix" Android's obnoxious Key-Value stores by defining Keys that have all info necessary to get/set values including a return Type, a defaultValue (if any) and any serialization/deserialization instructions.

Example:
```kotlin
object PrefKeys : PrefKeyNamespace(prefix = "com.sample.prefkey.") {
  val SOME_INT = key("someInt").int(default = 2)
  val SOME_NULLABLE_INT = key("nullableInt").int()
  val SOME_KOTLINX_SERIALIZED_OBJECT = key("myObj").json(SomeObject::serializer).async()
  val SOME_GSON_SERIALIZED_OBJECT = key("myObj2").gson<SomeOtherObject>(default = SomeOtherObject()).async()
}

val sharedPreferences: SharedPreference = TODO()

fun main() {
  // types are enforced by the keys
  val someInt: Int = sharedPreferences.get(PrefKeys.SOME_INT)
  val someNullableInt: Int? = sharedPreferences.get(PrefKeys.SOME_NULLABLE_INT)
  coroutineContext {
    val someObject: SomeObject? = sharedPreferences.get(PrefKeys.SOME_SERIALIZED_OBJECT)
    val someOtherObject: SomeOtherObject? = sharedPreferences.get(PrefKeys.SOME_GSON_SERIALIZED_OBJECT)
  }
}
```

Also works with Bundles...
```kotlin
object Arguments : BundleKeyNamespace(prefix = "com.sample.arguments.") {
  val SOME_INT = key("someInt").int(default = 2)
  val SOME_NULLABLE_INT = key("nullableInt").int()
}

val bundle: Bundle = TODO()

fun main() {
  // types are enforced by the keys
  val someInt: Int = bundle.get(Arguments.SOME_INT)
  val someNullableInt: Int? = bundle.get(Arguments.SOME_NULLABLE_INT)
}
```

Can also be used to define screens for Navigation-Compose, enabling type-safe navigation arguments.
```kotlin
object MyScreen : NavScreen(name = "myScreen") {
  val SOME_INT = key("someInt").int(default = 2)
  val SOME_NULLABLE_INT = key("nullableInt").int()
}

val savedStateHandle: SavedStateHandle = TODO()
val navController: NavController = TODO()

fun main() {
  // can pull nav arguments from either SavedStateHandles or Bundles
  val someInt: Int = savedStateHandle.get(MyScreen.SOME_INT)
  val someNullableInt: Int? = savedStateHandle.get(MyScreen.SOME_NULLABLE_INT)
  
  // type-safe navigation arguments
  navController.navigateTo(MyScreen) {
    set(MyScreen.SOME_INT, 5)
    set(MyScreen.SOME_NULLABLE_INT, 42)
  }
}
```



