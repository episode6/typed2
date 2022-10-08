package com.episode6.typed2

interface KeyData<T : Any?> {
  val name: String
  val rawType: RawType
  val nullable: Boolean
  val defaultValue: T?
}

sealed interface RawType {
  object Int : RawType
  object String : RawType
  // todo: more
}


class Key<RAW : Any?, T : Any?>(
  override val name: String,
  override val rawType: RawType,
  override val nullable: Boolean,
  val translateFromRaw: (RAW?) -> T?,
  val translateToRaw: (T?) -> RAW?,
  val defaultTranslation: T?,
) : KeyData<RAW> {
  override val defaultValue: RAW? get() = translateToRaw(defaultTranslation)
}

class AsyncKey<RAW : Any?, T : Any?>(
  override val name: String,
  override val rawType: RawType,
  override val nullable: Boolean,
  val translateFromRaw: suspend (RAW?) -> T?,
  val translateToRaw: suspend (T?) -> RAW?,
  val defaultTranslation: T?,
) : KeyData<RAW> {
  override val defaultValue: RAW? = null
}
