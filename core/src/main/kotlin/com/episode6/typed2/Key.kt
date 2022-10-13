package com.episode6.typed2

interface KeyValueGetter {
  fun contains(name: String): Boolean
}

interface KeyValueSetter

interface Key<T : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY: Any?> {
  val name: String
  fun getBackingData(getter: GETTER): BACKED_BY
  fun mapGet(backedBy: BACKED_BY): T
  fun setBackingData(setter: SETTER, value: BACKED_BY)
  fun mapSet(value: T): BACKED_BY
  fun get(getter: GETTER): T = mapGet(getBackingData(getter))
  fun set(setter: SETTER, value: T) = setBackingData(setter, mapSet(value))
}

interface KeyBuilder {
  val name: String
}

fun <T : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY: Any?> Key<T, GETTER, SETTER, BACKED_BY>.withDefault(
  default: () -> T,
): Key<T, GETTER, SETTER, BACKED_BY> = object : Key<T, GETTER, SETTER, BACKED_BY> {
  override val name: String get() = this@withDefault.name
  override fun getBackingData(getter: GETTER): BACKED_BY = this@withDefault.getBackingData(getter)
  override fun setBackingData(setter: SETTER, value: BACKED_BY) = this@withDefault.setBackingData(setter, value)
  override fun mapSet(value: T): BACKED_BY = this@withDefault.mapSet(value)
  override fun mapGet(backedBy: BACKED_BY): T = this@withDefault.mapGet(backedBy)

  override fun get(getter: GETTER): T = if (getter.contains(name)) this@withDefault.get(getter) else default()
}

fun <T : Any?, R : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY: Any?> Key<T, GETTER, SETTER, BACKED_BY>.mapType(
  mapGet: (T) -> R,
  mapSet: (R) -> T,
): Key<R, GETTER, SETTER, BACKED_BY> = object : Key<R, GETTER, SETTER, BACKED_BY> {
  override val name: String get() = this@mapType.name
  override fun getBackingData(getter: GETTER): BACKED_BY = this@mapType.getBackingData(getter)
  override fun setBackingData(setter: SETTER, value: BACKED_BY) = this@mapType.setBackingData(setter, value)

  override fun mapGet(backedBy: BACKED_BY): R = mapGet.invoke(this@mapType.mapGet(backedBy))
  override fun mapSet(value: R): BACKED_BY = this@mapType.mapSet(mapSet.invoke(value))
}

fun <T : Any, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY: Any?> Key<T?, GETTER, SETTER, BACKED_BY>.asNonNull(
  default: () -> T,
): Key<T, GETTER, SETTER, BACKED_BY> = mapType(mapGet = { it ?: default() }, mapSet = { it }).withDefault(default)
