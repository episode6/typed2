package com.episode6.typed2

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class AsyncKeyMapper<T : Any?, BACKED_BY : Any?> internal constructor(
  val mapGet: suspend (BACKED_BY) -> T,
  val mapSet: suspend (T) -> BACKED_BY,
)

class AsyncKey<T : Any?, BACKED_BY : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> internal constructor(
  override val name: String,
  override val default: OutputDefault<T>?,
  override val backingTypeInfo: KeyBackingTypeInfo<BACKED_BY>,
  val backer: KeyBacker<BACKED_BY, GETTER, SETTER>,
  val mapper: AsyncKeyMapper<T, BACKED_BY>,
  internal val newKeyCallback: (KeyTypeInfo<*, *>) -> Unit,
) : KeyTypeInfo<T, BACKED_BY> {
  init { newKeyCallback(this) }
}

fun <T : Any?, BACKED_BY : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> Key<T, BACKED_BY, GETTER, SETTER>.async(
  context: CoroutineContext = Dispatchers.Default,
): AsyncKey<T, BACKED_BY, GETTER, SETTER> = AsyncKey(
  name = name,
  default = default,
  backingTypeInfo = backingTypeInfo,
  backer = backer,
  mapper = mapper.toAsync(context),
  newKeyCallback = newKeyCallback,
)

suspend fun <T : Any?, BACKED_BY : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> AsyncKey<T, BACKED_BY, GETTER, SETTER>.get(
  getter: GETTER,
): T {
  val default = default?.provider()
  return if (default != null && !getter.contains(name)) default() else mapper.mapGet(backer.getBackingData(getter))
}

suspend fun <T : Any?, BACKED_BY : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> AsyncKey<T, BACKED_BY, GETTER, SETTER>.set(
  setter: SETTER,
  value: T,
) = backer.setBackingData(setter, mapper.mapSet(value))

private fun <T : Any?, BACKED_BY : Any?> KeyMapper<T, BACKED_BY>.toAsync(context: CoroutineContext) =
  AsyncKeyMapper<T, BACKED_BY>(
    mapGet = { withContext(context) { mapGet(it) } },
    mapSet = { withContext(context) { mapSet(it) } },
  )
