package com.episode6.typed2

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

data class AsyncKey<T : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY : Any?>(
  override val name: String,
  override val outputDefault: OutputDefault<T>?,
  override val backingTypeInfo: KeyBackingTypeInfo<BACKED_BY>,
  val getBackingData: suspend (GETTER) -> BACKED_BY,
  val mapGet: suspend (BACKED_BY) -> T,
  val setBackingData: suspend (SETTER, BACKED_BY) -> Unit,
  val mapSet: suspend (T) -> BACKED_BY,
) : KeyTypeInfo<T, BACKED_BY>

fun <T : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY : Any?> Key<T, GETTER, SETTER, BACKED_BY>.asAsync(
  context: CoroutineContext = Dispatchers.Default,
): AsyncKey<T, GETTER, SETTER, BACKED_BY> = AsyncKey(
  name = name,
  outputDefault = outputDefault,
  backingTypeInfo = backingTypeInfo,
  getBackingData = { withContext(context) { getBackingData(it) } },
  setBackingData = { setter, backedBy -> withContext(context) { setBackingData(setter, backedBy) } },
  mapGet = { withContext(context) { mapGet(it) } },
  mapSet = { withContext(context) { mapSet(it) } },
)

suspend fun <T : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY : Any?> AsyncKey<T, GETTER, SETTER, BACKED_BY>.get(
  getter: GETTER,
): T {
  val default = outputDefault?.provider()
  return if (default != null && !getter.contains(name)) default() else mapGet(getBackingData(getter))
}

suspend fun <T : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter, BACKED_BY : Any?> AsyncKey<T, GETTER, SETTER, BACKED_BY>.set(
  setter: SETTER,
  value: T,
) = setBackingData(setter, mapSet(value))
