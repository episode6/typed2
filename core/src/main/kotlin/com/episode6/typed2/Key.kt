package com.episode6.typed2

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

interface KeyValueGetter {
  fun contains(name: String): Boolean
}

interface KeyValueSetter

interface Key<T : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> {
  val name: String
  fun get(getter: GETTER): T
  fun set(setter: SETTER, value: T)
}

interface AsyncKey<T : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> {
  val name: String
  suspend fun get(getter: GETTER): T
  suspend fun set(setter: SETTER, value: T)
}

interface KeyBuilder {
  val name: String
}

fun <T : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> Key<T, GETTER, SETTER>.asAsync(
  context: CoroutineContext = Dispatchers.Default
): AsyncKey<T, GETTER, SETTER> = object : AsyncKey<T, GETTER, SETTER> {
  override val name: String = this@asAsync.name
  override suspend fun get(getter: GETTER): T = withContext(context) { this@asAsync.get(getter) }
  override suspend fun set(setter: SETTER, value: T) = withContext(context) { this@asAsync.set(setter, value) }
}

fun <T : Any?, R : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> Key<T, GETTER, SETTER>.mapType(
  default: ()-> R,
  mapGet: (T) -> R,
  mapSet: (R) -> T,
): Key<R, GETTER, SETTER> = object : Key<R, GETTER, SETTER> {
  override val name: String = this@mapType.name
  override fun get(getter: GETTER): R = if (!getter.contains(name)) default() else mapGet(this@mapType.get(getter))
  override fun set(setter: SETTER, value: R) = this@mapType.set(setter, mapSet(value))
}
