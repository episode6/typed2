package com.episode6.typed2

import kotlin.reflect.KClass

public sealed interface KeyDescriptor<T : Any?, BACKED_BY : Any?> {
  public val name: String
  public val backingTypeInfo: KeyBackingTypeInfo<BACKED_BY>
}

public data class KeyBackingTypeInfo<BACKED_BY : Any?>(public val kclass: KClass<*>, public val default: BACKED_BY, public val nullable: Boolean = default == null)
