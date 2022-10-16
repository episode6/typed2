package com.episode6.typed2

import kotlin.reflect.KType

interface KeyTypeInfo<T : Any?, BACKED_BY : Any?> {
  val name: String
  val outputDefault: OutputDefault<T>?
  val backingTypeInfo: KeyBackingTypeInfo<BACKED_BY>
}

data class KeyBackingTypeInfo<BACKED_BY : Any?>(val type: () -> KType, val default: BACKED_BY)

sealed interface OutputDefault<T> {
  data class Required<T>(val getError: () -> Throwable) : OutputDefault<T>
  data class Provider<T>(val get: () -> T) : OutputDefault<T>
}

fun <T> OutputDefault<T>.provider(): () -> T = when (this) {
  is OutputDefault.Required    -> {
    { throw getError() }
  }
  is OutputDefault.Provider<T> -> get
}


fun <T, R> OutputDefault<T>.map(mapGet: (T) -> R): OutputDefault<R> = when (this) {
  is OutputDefault.Required    -> OutputDefault.Required(getError)
  is OutputDefault.Provider<T> -> OutputDefault.Provider { mapGet(get()) }
}
