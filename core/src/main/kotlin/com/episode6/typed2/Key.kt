package com.episode6.typed2

interface KeyValueGetter {
  fun contains(name: String): Boolean
}

interface KeyValueSetter

interface Key<T : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> {
  val name: String
  fun get(getter: GETTER): T
  fun set(setter: SETTER, value: T)
}

interface KeyBuilder {
  val name: String
}

fun <T : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> Key<T, GETTER, SETTER>.withDefault(
  default: () -> T,
): Key<T, GETTER, SETTER> = object : Key<T, GETTER, SETTER> {
  override val name: String = this@withDefault.name
  override fun get(getter: GETTER): T = if (getter.contains(name)) this@withDefault.get(getter) else default()
  override fun set(setter: SETTER, value: T) = this@withDefault.set(setter, value)
}

fun <T : Any?, R : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> Key<T, GETTER, SETTER>.mapType(
  mapGet: (T) -> R,
  mapSet: (R) -> T,
): Key<R, GETTER, SETTER> = object : Key<R, GETTER, SETTER> {
  override val name: String = this@mapType.name
  override fun get(getter: GETTER): R = mapGet(this@mapType.get(getter))
  override fun set(setter: SETTER, value: R) = this@mapType.set(setter, mapSet(value))
}

fun <T : Any, GETTER : KeyValueGetter, SETTER : KeyValueSetter> Key<T?, GETTER, SETTER>.asNonNull(
  default: () -> T,
): Key<T, GETTER, SETTER> = mapType(
  mapGet = { it ?: default() },
  mapSet = { it }
).withDefault(default)
