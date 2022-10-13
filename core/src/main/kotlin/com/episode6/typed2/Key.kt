package com.episode6.typed2

interface KeyValueGetter {
  fun contains(name: String): Boolean
}

interface KeyValueSetter

typealias DefaultProvider<T> = ()->T

interface Key<T : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY : Any?> {
  val name: String
  val backingDefault: DefaultProvider<BACKED_BY>
  val default: DefaultProvider<T>? get() = null
  fun getBackingData(getter: GETTER): BACKED_BY
  fun mapGet(backedBy: BACKED_BY): T
  fun setBackingData(setter: SETTER, value: BACKED_BY)
  fun mapSet(value: T): BACKED_BY
//  fun get(getter: GETTER): T = mapGet(getBackingData(getter))
//  fun set(setter: SETTER, value: T) = setBackingData(setter, mapSet(value))
}

fun <T : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY : Any?> Key<T, GETTER, SETTER, BACKED_BY>.get(getter: GETTER): T  {
  val default = default
  return if (default != null && !getter.contains(name)) default() else mapGet(getBackingData(getter))
}

fun <T : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY : Any?> Key<T, GETTER, SETTER, BACKED_BY>.set(setter: SETTER, value: T) =
  setBackingData(setter, mapSet(value))

interface KeyBuilder {
  val name: String
}

fun <T : Any, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY : Any?> Key<T?, GETTER, SETTER, BACKED_BY>.withDefault(
  default: () -> T,
): Key<T, GETTER, SETTER, BACKED_BY> = object : Key<T, GETTER, SETTER, BACKED_BY> {
  override val name: String get() = this@withDefault.name
  override val default: DefaultProvider<T>? = default
  override val backingDefault: DefaultProvider<BACKED_BY> = this@withDefault.backingDefault
  override fun getBackingData(getter: GETTER): BACKED_BY = this@withDefault.getBackingData(getter)
  override fun setBackingData(setter: SETTER, value: BACKED_BY) = this@withDefault.setBackingData(setter, value)
  override fun mapSet(value: T): BACKED_BY = this@withDefault.mapSet(value)
  override fun mapGet(backedBy: BACKED_BY): T = this@withDefault.mapGet(backedBy) ?: default()
}

fun <T : Any?, R : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY : Any?> Key<T, GETTER, SETTER, BACKED_BY>.mapType(
  mapGet: (T) -> R,
  mapSet: (R) -> T,
): Key<R, GETTER, SETTER, BACKED_BY> = object : Key<R, GETTER, SETTER, BACKED_BY> {
  override val name: String get() = this@mapType.name
  override val backingDefault: DefaultProvider<BACKED_BY> = this@mapType.backingDefault
  override val default: DefaultProvider<R>? = this@mapType.default?.let { { mapGet(it.invoke()) } }
  override fun getBackingData(getter: GETTER): BACKED_BY = this@mapType.getBackingData(getter)
  override fun setBackingData(setter: SETTER, value: BACKED_BY) = this@mapType.setBackingData(setter, value)

  override fun mapGet(backedBy: BACKED_BY): R = mapGet.invoke(this@mapType.mapGet(backedBy))
  override fun mapSet(value: R): BACKED_BY = this@mapType.mapSet(mapSet.invoke(value))
}

internal fun <T : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> KeyBuilder.nativeKey(
  get: GETTER.() -> T,
  set: SETTER.(T) -> Unit,
  backingDefault: DefaultProvider<T>,
): Key<T, GETTER, SETTER, T> =
  object : Key<T, GETTER, SETTER, T> {
    override val name: String = this@nativeKey.name
    override val backingDefault: DefaultProvider<T> = backingDefault
    override fun mapGet(backedBy: T): T = backedBy
    override fun mapSet(value: T): T = value
    override fun getBackingData(getter: GETTER): T = get.invoke(getter)
    override fun setBackingData(setter: SETTER, value: T) = set.invoke(setter, value)
  }
