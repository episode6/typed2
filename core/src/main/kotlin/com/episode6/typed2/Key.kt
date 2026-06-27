package com.episode6.typed2

public class KeyBacker<BACKED_BY : Any?, in GETTER : KeyValueGetter, in SETTER : KeyValueSetter> internal constructor(
  public val getBackingData: (GETTER) -> BACKED_BY,
  public val setBackingData: (SETTER, BACKED_BY) -> Unit,
)

public class KeyMapper<T : Any?, BACKED_BY : Any?> internal constructor(
  public val mapGet: (BACKED_BY) -> T,
  public val mapSet: (T) -> BACKED_BY,
)

public class Key<T : Any?, BACKED_BY : Any?, in GETTER : KeyValueGetter, in SETTER : KeyValueSetter> internal constructor(
  override val name: String,
  internal val outputDefault: OutputDefault<T>?,
  override val backingTypeInfo: KeyBackingTypeInfo<BACKED_BY>,
  public val backer: KeyBacker<BACKED_BY, GETTER, SETTER>,
  public val mapper: KeyMapper<T, BACKED_BY>,
  internal val newKeyCallback: (KeyDescriptor<*, *>) -> Unit,
) : KeyDescriptor<T, BACKED_BY> {
  init {
    newKeyCallback(this)
  }
}

public interface KeyBuilder {
  public val name: String
  public val newKeyCallback: (KeyDescriptor<*, *>) -> Unit get() = {}
}
