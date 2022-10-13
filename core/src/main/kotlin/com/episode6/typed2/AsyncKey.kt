package com.episode6.typed2

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

interface AsyncKey<T : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY: Any?> {
  val name: String
  suspend fun get(getter: GETTER): T
  suspend fun set(setter: SETTER, value: T)
}

fun <T : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY: Any?> Key<T, GETTER, SETTER, BACKED_BY>.asAsync(
  context: CoroutineContext = Dispatchers.Default
): AsyncKey<T, GETTER, SETTER, BACKED_BY> = object : AsyncKey<T, GETTER, SETTER, BACKED_BY> {
  override val name: String = this@asAsync.name
  override suspend fun get(getter: GETTER): T = withContext(context) { this@asAsync.get(getter) }
  override suspend fun set(setter: SETTER, value: T) = withContext(context) { this@asAsync.set(setter, value) }
}
