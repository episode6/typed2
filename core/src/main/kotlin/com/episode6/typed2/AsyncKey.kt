package com.episode6.typed2

public class AsyncKeyMapper<T : Any?, BACKED_BY : Any?> internal constructor(
  public val mapGet: suspend (BACKED_BY) -> T,
  public val mapSet: suspend (T) -> BACKED_BY,
)

public class AsyncKey<T : Any?, BACKED_BY : Any?, in GETTER : KeyValueGetter, in SETTER : KeyValueSetter> internal constructor(
  override val name: String,
  internal val outputDefault: AsyncOutputDefault<T>?,
  override val backingTypeInfo: KeyBackingTypeInfo<BACKED_BY>,
  public val backer: KeyBacker<BACKED_BY, GETTER, SETTER>,
  public val mapper: AsyncKeyMapper<T, BACKED_BY>,
  internal val newKeyCallback: (KeyDescriptor<*, *>) -> Unit,
) : KeyDescriptor<T, BACKED_BY> {
  init {
    newKeyCallback(this)
  }
}
