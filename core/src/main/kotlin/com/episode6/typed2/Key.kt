package com.episode6.typed2

class KeyBacker<BACKED_BY : Any?, in GETTER : KeyValueGetter, in SETTER : KeyValueSetter> internal constructor(
  val getBackingData: (GETTER) -> BACKED_BY,
  val setBackingData: (SETTER, BACKED_BY) -> Unit,
)

class KeyMapper<T : Any?, BACKED_BY : Any?> internal constructor(
  val mapGet: (BACKED_BY) -> T,
  val mapSet: (T) -> BACKED_BY,
)

class Key<T : Any?, BACKED_BY : Any?, in GETTER : KeyValueGetter, in SETTER : KeyValueSetter> internal constructor(
  override val name: String,
  internal val outputDefault: OutputDefault<T>?,
  override val backingTypeInfo: KeyBackingTypeInfo<BACKED_BY>,
  val backer: KeyBacker<BACKED_BY, GETTER, SETTER>,
  val mapper: KeyMapper<T, BACKED_BY>,
  internal val newKeyCallback: (KeyDescriptor<*, *>) -> Unit,
) : KeyDescriptor<T, BACKED_BY> {
  init {
    newKeyCallback(this)
  }
}

interface KeyBuilder {
  val name: String
  val newKeyCallback: (KeyDescriptor<*, *>) -> Unit get() = {}
}
