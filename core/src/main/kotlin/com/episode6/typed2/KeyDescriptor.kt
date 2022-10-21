package com.episode6.typed2

import kotlin.reflect.KClass

interface KeyDescriptor<T : Any?, BACKED_BY : Any?> {
  val name: String
  val outputDefault: OutputDefault<T>?
  val backingTypeInfo: KeyBackingTypeInfo<BACKED_BY>
}

data class KeyBackingTypeInfo<BACKED_BY : Any?>(val kclass: KClass<*>, val default: BACKED_BY, val nullable: Boolean = default == null)

sealed class OutputDefault<T> {
  internal class Required<T>(val getError: () -> Throwable) : OutputDefault<T>()
  internal class Provider<T>(val get: () -> T) : OutputDefault<T>()
}

fun <T> OutputDefault<T>.provider(): () -> T = when (this) {
  is OutputDefault.Required    -> {
    { throw getError() }
  }
  is OutputDefault.Provider<T> -> get
}

val KeyDescriptor<*, *>.isRequired: Boolean get() = outputDefault is OutputDefault.Required

@Suppress("UNCHECKED_CAST")
fun <T, R> OutputDefault<T>.map(mapGet: (T) -> R): OutputDefault<R> = when (this) {
  is OutputDefault.Required    -> this as OutputDefault.Required<R>
  is OutputDefault.Provider<T> -> OutputDefault.Provider { mapGet(get()) }
}