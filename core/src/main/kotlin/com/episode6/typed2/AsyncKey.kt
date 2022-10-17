package com.episode6.typed2

class AsyncKeyMapper<T : Any?, BACKED_BY : Any?> internal constructor(
  val mapGet: suspend (BACKED_BY) -> T,
  val mapSet: suspend (T) -> BACKED_BY,
)

class AsyncKey<T : Any?, BACKED_BY : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> internal constructor(
  override val name: String,
  override val default: OutputDefault<T>?,
  override val backingTypeInfo: KeyBackingTypeInfo<BACKED_BY>,
  val backer: KeyBacker<BACKED_BY, GETTER, SETTER>,
  val mapper: AsyncKeyMapper<T, BACKED_BY>,
  internal val newKeyCallback: (KeyTypeInfo<*, *>) -> Unit,
) : KeyTypeInfo<T, BACKED_BY> {
  init {
    newKeyCallback(this)
  }
}

suspend fun <T : Any?, BACKED_BY : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> AsyncKey<T, BACKED_BY, GETTER, SETTER>.get(
  getter: GETTER,
): T {
  val default = default?.provider()
  return if (default != null && !getter.contains(name)) default() else mapper.mapGet(backer.getBackingData(getter))
}

suspend fun <T : Any?, BACKED_BY : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> AsyncKey<T, BACKED_BY, GETTER, SETTER>.set(
  setter: SETTER,
  value: T,
) = backer.setBackingData(setter, mapper.mapSet(value))
