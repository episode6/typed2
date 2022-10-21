package com.episode6.typed2

import kotlin.reflect.KClass

object NativeKeys {
  fun <T : Any, GETTER : KeyValueGetter, SETTER : KeyValueSetter> create(
    name: String,
    backingDefault: T,
    backingClass: KClass<T>,
    get: GETTER.() -> T,
    set: SETTER.(T) -> Unit,
    newKeyCallback: (KeyTypeInfo<*, *>) -> Unit
  ): Key<T, T, GETTER, SETTER> = Key(
    name = name,
    default = null,
    backingTypeInfo = KeyBackingTypeInfo(kclass = backingClass, default = backingDefault),
    backer = KeyBacker(getBackingData = get, setBackingData = set),
    mapper = KeyMapper({ it }, { it }),
    newKeyCallback = newKeyCallback
  )
  fun <T : Any, GETTER : KeyValueGetter, SETTER : KeyValueSetter> create(
    name: String,
    backingClass: KClass<T>,
    get: GETTER.() -> T?,
    set: SETTER.(T?) -> Unit,
    newKeyCallback: (KeyTypeInfo<*, *>) -> Unit
  ): Key<T?, T?, GETTER, SETTER> = Key(
    name = name,
    default = null,
    backingTypeInfo = KeyBackingTypeInfo(kclass = backingClass, default = null),
    backer = KeyBacker(getBackingData = get, setBackingData = set),
    mapper = KeyMapper({ it }, { it }),
    newKeyCallback = newKeyCallback
  )
}

internal inline fun <reified T : Any, GETTER : KeyValueGetter, SETTER : KeyValueSetter> KeyBuilder.nativeKey(
  noinline get: GETTER.() -> T,
  noinline set: SETTER.(T) -> Unit,
  backingDefault: T,
): Key<T, T, GETTER, SETTER> = NativeKeys.create(
  name = name,
  backingDefault = backingDefault,
  backingClass = T::class,
  get = get,
  set = set,
  newKeyCallback = newKeyCallback,
)

internal inline fun <reified T : Any, GETTER : KeyValueGetter, SETTER : KeyValueSetter> KeyBuilder.nativeKey(
  noinline get: GETTER.() -> T?,
  noinline set: SETTER.(T?) -> Unit,
): Key<T?, T?, GETTER, SETTER> = NativeKeys.create(
  name = name,
  backingClass = T::class,
  get = get,
  set = set,
  newKeyCallback = newKeyCallback,
)
