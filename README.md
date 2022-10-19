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

This pattern is currently working for SharedPrefs and Bundles, but I'd like to expand it to navigation arguments and SavedStateHandle's getStateFlow/getLiveData methods. Only a handful of types are currently implemented, while I work through the complexities of these other use-cases.
