package com.episode6.typed2

interface KeyValueGetter {
  fun contains(name: String): Boolean
}

interface KeyValueSetter

typealias DefaultProvider<T> = () -> T

data class Key<T : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY : Any?>(
  val name: String,
  val backingDefault: DefaultProvider<BACKED_BY>,
  val default: DefaultProvider<T>? = null,
  val getBackingData: (GETTER) -> BACKED_BY,
  val mapGet: (BACKED_BY) -> T,
  val setBackingData: (SETTER, BACKED_BY) -> Unit,
  val mapSet: (T) -> BACKED_BY,
)

fun <T : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY : Any?> Key<T, GETTER, SETTER, BACKED_BY>.get(
  getter: GETTER,
): T {
  val default = default
  return if (default != null && !getter.contains(name)) default() else mapGet(getBackingData(getter))
}

fun <T : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY : Any?> Key<T, GETTER, SETTER, BACKED_BY>.set(
  setter: SETTER,
  value: T,
) =
  setBackingData(setter, mapSet(value))

interface KeyBuilder {
  val name: String
}

fun <T : Any, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY : Any?> Key<T?, GETTER, SETTER, BACKED_BY>.withDefault(
  default: () -> T,
): Key<T, GETTER, SETTER, BACKED_BY> = Key(
  name = name,
  default = default,
  backingDefault = backingDefault,
  getBackingData = getBackingData,
  setBackingData = setBackingData,
  mapSet = mapSet,
  mapGet = { mapGet(it) ?: default() }
)

fun <T : Any?, R : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY : Any?> Key<T, GETTER, SETTER, BACKED_BY>.mapType(
  mapGet: (T) -> R,
  mapSet: (R) -> T,
): Key<R, GETTER, SETTER, BACKED_BY> = Key(
  name = name,
  default = default?.let { { mapGet(it()) } },
  backingDefault = backingDefault,
  getBackingData = getBackingData,
  setBackingData = setBackingData,
  mapSet = { this@mapType.mapSet(mapSet(it)) },
  mapGet = { mapGet(this@mapType.mapGet(it)) }
)

internal fun <T : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> KeyBuilder.nativeKey(
  get: GETTER.() -> T,
  set: SETTER.(T) -> Unit,
  backingDefault: DefaultProvider<T>,
): Key<T, GETTER, SETTER, T> = Key(
  name = name,
  backingDefault = backingDefault,
  mapGet = { it },
  mapSet = { it },
  getBackingData = get,
  setBackingData = set,
)
