package com.episode6.typed2

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

interface AsyncKey<T : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY : Any?> {
  val name: String
  val backingDefault: DefaultProvider<BACKED_BY>
  val default: DefaultProvider<T>? get() = null
  fun getBackingData(getter: GETTER): BACKED_BY
  suspend fun mapGet(backedBy: BACKED_BY): T
  fun setBackingData(setter: SETTER, value: BACKED_BY)
  suspend fun mapSet(value: T): BACKED_BY
}

fun <T : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY : Any?> Key<T, GETTER, SETTER, BACKED_BY>.asAsync(
  context: CoroutineContext = Dispatchers.Default,
): AsyncKey<T, GETTER, SETTER, BACKED_BY> = object : AsyncKey<T, GETTER, SETTER, BACKED_BY> {
  override val name: String = this@asAsync.name
  override val backingDefault: DefaultProvider<BACKED_BY> = this@asAsync.backingDefault
  override val default: DefaultProvider<T>? = this@asAsync.default
  override fun getBackingData(getter: GETTER): BACKED_BY = this@asAsync.getBackingData(getter)
  override fun setBackingData(setter: SETTER, value: BACKED_BY) = this@asAsync.setBackingData(setter, value)
  override suspend fun mapGet(backedBy: BACKED_BY): T = withContext(context) { this@asAsync.mapGet(backedBy) }
  override suspend fun mapSet(value: T): BACKED_BY = withContext(context) { this@asAsync.mapSet(value) }
}

suspend fun <T : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY : Any?> AsyncKey<T, GETTER, SETTER, BACKED_BY>.get(
  getter: GETTER,
): T {
  val default = default
  return if (default != null && !getter.contains(name)) default() else mapGet(getBackingData(getter))
}

suspend fun <T : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY : Any?> AsyncKey<T, GETTER, SETTER, BACKED_BY>.set(
  setter: SETTER,
  value: T,
) = setBackingData(setter, mapSet(value))
