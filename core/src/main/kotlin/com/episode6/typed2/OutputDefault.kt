package com.episode6.typed2

val KeyDescriptor<*, *>.isRequired: Boolean
  get() = when (this) {
    is Key<*, *, *, *>      -> outputDefault is OutputDefault.Required
    is AsyncKey<*, *, *, *> -> outputDefault is AsyncOutputDefault.Required
  }

fun <T> Key<T, *, *, *>.outputDefaultProvider(): (() -> T)? = outputDefault?.provider()
fun <T> AsyncKey<T, *, *, *>.outputDefaultProvider(): (suspend () -> T)? = outputDefault?.provider()

internal sealed class OutputDefault<T> {
  internal class Required<T>(val getError: () -> Throwable) : OutputDefault<T>()
  internal class Provider<T>(val get: () -> T) : OutputDefault<T>()
}

internal sealed class AsyncOutputDefault<T> {
  internal class Required<T>(val getError: () -> Throwable) : AsyncOutputDefault<T>()
  internal class Provider<T>(val get: suspend () -> T) : AsyncOutputDefault<T>()
}

internal fun <T> OutputDefault<T>.provider(): () -> T = when (this) {
  is OutputDefault.Required    -> {
    { throw getError() }
  }
  is OutputDefault.Provider<T> -> {
    { get() }
  }
}

internal fun <T> AsyncOutputDefault<T>.provider(): suspend () -> T = when (this) {
  is AsyncOutputDefault.Required    -> {
    { throw getError() }
  }
  is AsyncOutputDefault.Provider<T> -> {
    { get() }
  }
}

@Suppress("UNCHECKED_CAST")
internal fun <T, R> OutputDefault<T>.map(mapGet: (T) -> R): OutputDefault<R> = when (this) {
  is OutputDefault.Required    -> this as OutputDefault.Required<R>
  is OutputDefault.Provider<T> -> OutputDefault.Provider { mapGet(get()) }
}

@Suppress("UNCHECKED_CAST")
internal fun <T, R> AsyncOutputDefault<T>.map(mapGet: suspend (T) -> R): AsyncOutputDefault<R> = when (this) {
  is AsyncOutputDefault.Required    -> this as AsyncOutputDefault.Required<R>
  is AsyncOutputDefault.Provider<T> -> AsyncOutputDefault.Provider { mapGet(get()) }
}

internal fun <T> OutputDefault<T>.async(): AsyncOutputDefault<T> = when (this) {
  is OutputDefault.Required -> AsyncOutputDefault.Required(getError)
  is OutputDefault.Provider -> AsyncOutputDefault.Provider { get() }
}
