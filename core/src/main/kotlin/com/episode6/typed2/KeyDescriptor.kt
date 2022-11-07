package com.episode6.typed2

import kotlin.reflect.KClass

sealed interface KeyDescriptor<T : Any?, BACKED_BY : Any?> {
  val name: String
  val backingTypeInfo: KeyBackingTypeInfo<BACKED_BY>
}

data class KeyBackingTypeInfo<BACKED_BY : Any?>(val kclass: KClass<*>, val default: BACKED_BY, val nullable: Boolean = default == null)
