package com.episode6.typed2

interface KeyValueGetter {
  fun contains(name: String): Boolean
}

interface KeyValueSetter {
  fun remove(name: String)
}

class KeyBacker<BACKED_BY : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> internal constructor(
  val getBackingData: (GETTER) -> BACKED_BY,
  val setBackingData: (SETTER, BACKED_BY) -> Unit,
)

class KeyMapper<T : Any?, BACKED_BY : Any?> internal constructor(
  val mapGet: (BACKED_BY) -> T,
  val mapSet: (T) -> BACKED_BY,
)

class Key<T : Any?, BACKED_BY : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> internal constructor(
  override val name: String,
  override val outputDefault: OutputDefault<T>?,
  override val backingTypeInfo: KeyBackingTypeInfo<BACKED_BY>,
  val backer: KeyBacker<BACKED_BY, GETTER, SETTER>,
  val mapper: KeyMapper<T, BACKED_BY>,
  internal val newKeyCallback: (KeyTypeInfo<*, *>) -> Unit,
) : KeyTypeInfo<T, BACKED_BY> {
  init {
    newKeyCallback(this)
  }
}

fun <T : Any?, BACKED_BY : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> Key<T, BACKED_BY, GETTER, SETTER>.get(
  getter: GETTER,
): T {
  val default = outputDefault?.provider()
  return if (default != null && !getter.contains(name)) default() else mapper.mapGet(backer.getBackingData(getter))
}

fun <T : Any?, BACKED_BY : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> Key<T, BACKED_BY, GETTER, SETTER>.set(
  setter: SETTER,
  value: T,
) = backer.setBackingData(setter, mapper.mapSet(value))

interface KeyBuilder {
  val name: String
  val newKeyCallback: (KeyTypeInfo<*, *>) -> Unit get() = {}
}
