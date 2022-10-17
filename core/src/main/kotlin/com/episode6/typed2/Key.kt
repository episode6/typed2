package com.episode6.typed2

interface KeyValueGetter {
  fun contains(name: String): Boolean
}

interface KeyValueSetter

class KeyBacker<GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY : Any?> internal constructor(
  val getBackingData: (GETTER) -> BACKED_BY,
  val setBackingData: (SETTER, BACKED_BY) -> Unit,
)

class KeyMapper<T : Any?, BACKED_BY : Any?> internal constructor(
  val mapGet: (BACKED_BY) -> T,
  val mapSet: (T) -> BACKED_BY,
)

class Key<T : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY : Any?> internal constructor(
  override val name: String,
  override val default: OutputDefault<T>?,
  override val backingTypeInfo: KeyBackingTypeInfo<BACKED_BY>,
  val backer: KeyBacker<GETTER, SETTER, BACKED_BY>,
  val mapper: KeyMapper<T, BACKED_BY>,
  val newKeyCallback: (KeyTypeInfo<*, *>) -> Unit,
) : KeyTypeInfo<T, BACKED_BY> {
  init { newKeyCallback(this) }
}

fun <T : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY : Any?> Key<T, GETTER, SETTER, BACKED_BY>.get(
  getter: GETTER,
): T {
  val default = default?.provider()
  return if (default != null && !getter.contains(name)) default() else mapper.mapGet(backer.getBackingData(getter))
}

fun <T : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY : Any?> Key<T, GETTER, SETTER, BACKED_BY>.set(
  setter: SETTER,
  value: T,
) = backer.setBackingData(setter, mapper.mapSet(value))

interface KeyBuilder {
  val name: String
  val newKeyCallback: (KeyTypeInfo<*, *>) -> Unit get() = {}
}

fun <T : Any, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY : Any?> Key<T?, GETTER, SETTER, BACKED_BY>.withDefault(
  default: ()->T
): Key<T, GETTER, SETTER, BACKED_BY> = withOutputDefault(OutputDefault.Provider(default))

fun <T : Any, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY : Any?> Key<T?, GETTER, SETTER, BACKED_BY>.withOutputDefault(
  default: OutputDefault<T>,
): Key<T, GETTER, SETTER, BACKED_BY> = Key(
  name = name,
  backingTypeInfo = backingTypeInfo,
  backer = backer,
  default = default,
  mapper = KeyMapper(
    mapSet = mapper.mapSet,
    mapGet = { mapper.mapGet(it) ?: default.provider().invoke() }
  ),
  newKeyCallback = newKeyCallback,
)

fun <T : Any?, R : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY : Any?> Key<T, GETTER, SETTER, BACKED_BY>.mapType(
  mapGet: (T) -> R,
  mapSet: (R) -> T,
): Key<R, GETTER, SETTER, BACKED_BY> = Key(
  name = name,
  backingTypeInfo = backingTypeInfo,
  backer = backer,
  default = default?.map(mapGet),
  mapper = KeyMapper(
    mapSet = { mapper.mapSet(mapSet(it)) },
    mapGet = { mapGet(mapper.mapGet(it)) }
  ),
  newKeyCallback = newKeyCallback,
)

internal inline fun <reified T : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> KeyBuilder.nativeKey(
  noinline get: GETTER.() -> T,
  noinline set: SETTER.(T) -> Unit,
  backingDefault: T,
): Key<T, GETTER, SETTER, T> = Key(
  name = name,
  default = null,
  backingTypeInfo = KeyBackingTypeInfo(kclass = T::class, default = backingDefault),
  backer = KeyBacker(getBackingData = get, setBackingData = set),
  mapper = KeyMapper({ it }, { it }),
  newKeyCallback = newKeyCallback
)
