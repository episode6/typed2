package com.episode6.typed2

import kotlin.reflect.KClass

object NativeKeys {
  fun <T : Any, GETTER : KeyValueGetter, SETTER : KeyValueSetter> create(
    keyBuilder: KeyBuilder,
    backingDefault: T,
    backingClass: KClass<T>,
    get: GETTER.() -> T,
    set: SETTER.(T) -> Unit,
  ): Key<T, T, GETTER, SETTER> = Key(
    name = keyBuilder.name,
    outputDefault = null,
    backingTypeInfo = KeyBackingTypeInfo(kclass = backingClass, default = backingDefault),
    backer = KeyBacker(getBackingData = get, setBackingData = set),
    mapper = KeyMapper({ it }, { it }),
    newKeyCallback = keyBuilder.newKeyCallback
  )

  fun <T : Any, GETTER : KeyValueGetter, SETTER : KeyValueSetter> create(
    keyBuilder: KeyBuilder,
    backingClass: KClass<T>,
    get: GETTER.() -> T?,
    set: SETTER.(T?) -> Unit,
  ): Key<T?, T?, GETTER, SETTER> = Key(
    name = keyBuilder.name,
    outputDefault = null,
    backingTypeInfo = KeyBackingTypeInfo(kclass = backingClass, default = null),
    backer = KeyBacker(getBackingData = get, setBackingData = set),
    mapper = KeyMapper({ it }, { it }),
    newKeyCallback = keyBuilder.newKeyCallback
  )

  inline fun <reified T : Any, GETTER : KeyValueGetter, SETTER : KeyValueSetter> create(
    keyBuilder: KeyBuilder,
    backingDefault: T,
    noinline get: GETTER.() -> T,
    noinline set: SETTER.(T) -> Unit,
  ): Key<T, T, GETTER, SETTER> = create(
    keyBuilder,
    backingDefault = backingDefault,
    backingClass = T::class,
    get = get,
    set = set,
  )

  inline fun <reified T : Any, GETTER : KeyValueGetter, SETTER : KeyValueSetter> create(
    keyBuilder: KeyBuilder,
    noinline get: GETTER.() -> T?,
    noinline set: SETTER.(T?) -> Unit,
  ): Key<T?, T?, GETTER, SETTER> = create(
    keyBuilder,
    backingClass = T::class,
    get = get,
    set = set,
  )

  fun <T : Any, GETTER : KeyValueGetter, SETTER : KeyValueSetter> createAsync(
    keyBuilder: KeyBuilder,
    backingDefault: T,
    backingClass: KClass<T>,
    get: suspend GETTER.() -> T,
    set: suspend SETTER.(T) -> Unit,
  ): AsyncKey<T, T, GETTER, SETTER> = AsyncKey(
    name = keyBuilder.name,
    outputDefault = null,
    backingTypeInfo = KeyBackingTypeInfo(kclass = backingClass, default = backingDefault),
    backer = AsyncKeyBacker(getBackingData = get, setBackingData = set),
    mapper = AsyncKeyMapper({ it }, { it }),
    newKeyCallback = keyBuilder.newKeyCallback
  )

  fun <T : Any, GETTER : KeyValueGetter, SETTER : KeyValueSetter> createAsync(
    keyBuilder: KeyBuilder,
    backingClass: KClass<T>,
    get: suspend GETTER.() -> T?,
    set: suspend SETTER.(T?) -> Unit,
  ): AsyncKey<T?, T?, GETTER, SETTER> = AsyncKey(
    name = keyBuilder.name,
    outputDefault = null,
    backingTypeInfo = KeyBackingTypeInfo(kclass = backingClass, default = null),
    backer = AsyncKeyBacker(getBackingData = get, setBackingData = set),
    mapper = AsyncKeyMapper({ it }, { it }),
    newKeyCallback = keyBuilder.newKeyCallback
  )

  inline fun <reified T : Any, GETTER : KeyValueGetter, SETTER : KeyValueSetter> createAsync(
    keyBuilder: KeyBuilder,
    backingDefault: T,
    noinline get: suspend GETTER.() -> T,
    noinline set: suspend SETTER.(T) -> Unit,
  ): AsyncKey<T, T, GETTER, SETTER> = createAsync(
    keyBuilder,
    backingDefault = backingDefault,
    backingClass = T::class,
    get = get,
    set = set,
  )

  inline fun <reified T : Any, GETTER : KeyValueGetter, SETTER : KeyValueSetter> createAsync(
    keyBuilder: KeyBuilder,
    noinline get: suspend GETTER.() -> T?,
    noinline set: suspend SETTER.(T?) -> Unit,
  ): AsyncKey<T?, T?, GETTER, SETTER> = createAsync(
    keyBuilder,
    backingClass = T::class,
    get = get,
    set = set,
  )
}

internal inline fun <reified T : Any, GETTER : KeyValueGetter, SETTER : KeyValueSetter> KeyBuilder.nativeKey(
  noinline get: GETTER.() -> T,
  noinline set: SETTER.(T) -> Unit,
  backingDefault: T,
): Key<T, T, GETTER, SETTER> = NativeKeys.create(
  this,
  backingDefault = backingDefault,
  get = get,
  set = set,
)

internal inline fun <reified T : Any, GETTER : KeyValueGetter, SETTER : KeyValueSetter> KeyBuilder.nativeKey(
  noinline get: GETTER.() -> T?,
  noinline set: SETTER.(T?) -> Unit,
): Key<T?, T?, GETTER, SETTER> = NativeKeys.create(
  this,
  get = get,
  set = set,
)

internal inline fun <reified T : Any, GETTER : KeyValueGetter, SETTER : KeyValueSetter> KeyBuilder.nativeAsyncKey(
  noinline get: suspend GETTER.() -> T,
  noinline set: suspend SETTER.(T) -> Unit,
  backingDefault: T,
): AsyncKey<T, T, GETTER, SETTER> = NativeKeys.createAsync(
  this,
  backingDefault = backingDefault,
  get = get,
  set = set,
)

internal inline fun <reified T : Any, GETTER : KeyValueGetter, SETTER : KeyValueSetter> KeyBuilder.nativeAsyncKey(
  noinline get: suspend GETTER.() -> T?,
  noinline set: suspend SETTER.(T?) -> Unit,
): AsyncKey<T?, T?, GETTER, SETTER> = NativeKeys.createAsync(
  this,
  get = get,
  set = set,
)
