package com.episode6.typed2

import kotlin.reflect.typeOf

interface KeyValueGetter {
  fun contains(name: String): Boolean
}

interface KeyValueSetter

data class Key<T : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY : Any?>(
  override val name: String,
  override val outputDefault: OutputDefault<T>?,
  override val backingTypeInfo: KeyBackingTypeInfo<BACKED_BY>,
  val getBackingData: (GETTER) -> BACKED_BY,
  val mapGet: (BACKED_BY) -> T,
  val setBackingData: (SETTER, BACKED_BY) -> Unit,
  val mapSet: (T) -> BACKED_BY,
) : KeyTypeInfo<T, BACKED_BY>

fun <T : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY : Any?> Key<T, GETTER, SETTER, BACKED_BY>.get(
  getter: GETTER,
): T {
  val default = outputDefault?.provider()
  return if (default != null && !getter.contains(name)) default() else mapGet(getBackingData(getter))
}

fun <T : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY : Any?> Key<T, GETTER, SETTER, BACKED_BY>.set(
  setter: SETTER,
  value: T,
) = setBackingData(setter, mapSet(value))

interface KeyBuilder {
  val name: String
}

inline fun <reified T : Any, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY : Any?> Key<T?, GETTER, SETTER, BACKED_BY>.withDefault(
  default: OutputDefault<T>,
): Key<T, GETTER, SETTER, BACKED_BY> = Key(
  name = name,
  outputDefault = default,
  backingTypeInfo = backingTypeInfo,
  getBackingData = getBackingData,
  setBackingData = setBackingData,
  mapSet = mapSet,
  mapGet = { mapGet(it) ?: default.provider().invoke() }
)

inline fun <T : Any?, reified R : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY : Any?> Key<T, GETTER, SETTER, BACKED_BY>.mapType(
  noinline mapGet: (T) -> R,
  noinline mapSet: (R) -> T,
): Key<R, GETTER, SETTER, BACKED_BY> = Key(
  name = name,
  backingTypeInfo = backingTypeInfo,
  getBackingData = getBackingData,
  setBackingData = setBackingData,
  outputDefault = outputDefault?.map(mapGet),
  mapSet = { this@mapType.mapSet(mapSet(it)) },
  mapGet = { mapGet(this@mapType.mapGet(it)) }
)

internal inline fun <reified T : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> KeyBuilder.nativeKey(
  noinline get: GETTER.() -> T,
  noinline set: SETTER.(T) -> Unit,
  backingDefault: T,
): Key<T, GETTER, SETTER, T> = Key(
  name = name,
  outputDefault = null,
  backingTypeInfo = KeyBackingTypeInfo(type = { typeOf<T>() }, default = backingDefault),
  mapGet = { it },
  mapSet = { it },
  getBackingData = get,
  setBackingData = set,
)
