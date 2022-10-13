package com.episode6.typed2

interface KeyValueGetter {
  fun contains(name: String): Boolean
}

interface KeyValueSetter

interface Key<T : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY: Any?> {
  val name: String
  fun get(getter: GETTER): T
  fun set(setter: SETTER, value: T)
}

interface KeyBuilder {
  val name: String
}

fun <T : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY: Any?> Key<T, GETTER, SETTER, BACKED_BY>.withDefault(
  default: () -> T,
): Key<T, GETTER, SETTER, BACKED_BY> = object : Key<T, GETTER, SETTER, BACKED_BY> {
  override val name: String = this@withDefault.name
  override fun get(getter: GETTER): T = if (getter.contains(name)) this@withDefault.get(getter) else default()
  override fun set(setter: SETTER, value: T) = this@withDefault.set(setter, value)
}

fun <T : Any?, R : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY: Any?> Key<T, GETTER, SETTER, BACKED_BY>.mapType(
  mapGet: (T) -> R,
  mapSet: (R) -> T,
): Key<R, GETTER, SETTER, BACKED_BY> = object : Key<R, GETTER, SETTER, BACKED_BY> {
  override val name: String = this@mapType.name
  override fun get(getter: GETTER): R = mapGet(this@mapType.get(getter))
  override fun set(setter: SETTER, value: R) = this@mapType.set(setter, mapSet(value))
}

fun <T : Any, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY: Any?> Key<T?, GETTER, SETTER, BACKED_BY>.asNonNull(
  default: () -> T,
): Key<T, GETTER, SETTER, BACKED_BY> = mapType(
  mapGet = { it ?: default() },
  mapSet = { it }
).withDefault(default)
