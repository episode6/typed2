package com.episode6.typed2

import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


fun <T : Any, BACKED_BY : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> Key<T?, BACKED_BY, GETTER, SETTER>.defaultProvider(
  default: () -> T,
): Key<T, BACKED_BY, GETTER, SETTER> = withOutputDefault(OutputDefault.Provider(default))

fun <T : Any, BACKED_BY : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> AsyncKey<T?, BACKED_BY, GETTER, SETTER>.defaultProvider(
  default: suspend () -> T,
): AsyncKey<T, BACKED_BY, GETTER, SETTER> = withOutputDefault(AsyncOutputDefault.Provider(default))

internal fun <T : Any, BACKED_BY : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> Key<T?, BACKED_BY, GETTER, SETTER>.withOutputDefault(
  default: OutputDefault<T>,
): Key<T, BACKED_BY, GETTER, SETTER> = Key(
  name = name,
  backingTypeInfo = backingTypeInfo,
  backer = backer,
  outputDefault = default,
  mapper = KeyMapper(
    mapSet = mapper.mapSet,
    mapGet = { mapper.mapGet(it) ?: default.provider().invoke() }
  ),
  newKeyCallback = newKeyCallback,
)

internal fun <T : Any, BACKED_BY : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> AsyncKey<T?, BACKED_BY, GETTER, SETTER>.withOutputDefault(
  default: AsyncOutputDefault<T>,
): AsyncKey<T, BACKED_BY, GETTER, SETTER> = AsyncKey(
  name = name,
  backingTypeInfo = backingTypeInfo,
  backer = backer,
  outputDefault = default,
  mapper = AsyncKeyMapper(
    mapSet = mapper.mapSet,
    mapGet = { mapper.mapGet(it) ?: default.provider().invoke() }
  ),
  newKeyCallback = newKeyCallback,
)

fun <T : Any?, R : Any?, BACKED_BY : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> Key<T, BACKED_BY, GETTER, SETTER>.mapType(
  mapGet: (T) -> R,
  mapSet: (R) -> T,
): Key<R, BACKED_BY, GETTER, SETTER> = Key(
  name = name,
  backingTypeInfo = backingTypeInfo,
  backer = backer,
  outputDefault = outputDefault?.map(mapGet),
  mapper = KeyMapper(
    mapSet = { mapper.mapSet(mapSet(it)) },
    mapGet = { mapGet(mapper.mapGet(it)) }
  ),
  newKeyCallback = newKeyCallback,
)

fun <T : Any?, R : Any?, BACKED_BY : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> AsyncKey<T, BACKED_BY, GETTER, SETTER>.mapType(
  mapGet: suspend (T) -> R,
  mapSet: suspend (R) -> T,
): AsyncKey<R, BACKED_BY, GETTER, SETTER> = AsyncKey(
  name = name,
  backingTypeInfo = backingTypeInfo,
  backer = backer,
  outputDefault = outputDefault?.map(mapGet),
  mapper = AsyncKeyMapper(
    mapSet = { mapper.mapSet(mapSet(it)) },
    mapGet = { mapGet(mapper.mapGet(it)) }
  ),
  newKeyCallback = newKeyCallback,
)

fun <T : Any?, BACKED_BY : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> Key<T, BACKED_BY, GETTER, SETTER>.async(
  mapperContext: CoroutineContext,
  backerContext: CoroutineContext,
): AsyncKey<T, BACKED_BY, GETTER, SETTER> = AsyncKey(
  name = name,
  outputDefault = outputDefault?.async(),
  backingTypeInfo = backingTypeInfo,
  backer = backer.async(backerContext),
  mapper = mapper.async(mapperContext),
  newKeyCallback = newKeyCallback,
)

fun <T : Any?, BACKED_BY : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> AsyncKey<T, BACKED_BY, GETTER, SETTER>.asyncContext(
  mapperContext: CoroutineContext = EmptyCoroutineContext,
  backerContext: CoroutineContext = EmptyCoroutineContext,
): AsyncKey<T, BACKED_BY, GETTER, SETTER> = AsyncKey(
  name = name,
  outputDefault = outputDefault,
  backingTypeInfo = backingTypeInfo,
  backer = backer.async(backerContext),
  mapper = mapper.async(mapperContext),
  newKeyCallback = newKeyCallback,
)

private fun <T : Any?, BACKED_BY : Any?> KeyMapper<T, BACKED_BY>.async(context: CoroutineContext) = AsyncKeyMapper<T, BACKED_BY>(
  mapGet = { withContext(context) { mapGet(it) } },
  mapSet = { withContext(context) { mapSet(it) } },
)

private fun <BACKED_BY : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> KeyBacker<BACKED_BY, GETTER, SETTER>.async(
  context: CoroutineContext,
) = AsyncKeyBacker<BACKED_BY, GETTER, SETTER>(
  getBackingData = { withContext(context) { getBackingData(it) } },
  setBackingData = { setter, backedBy -> withContext(context) { setBackingData(setter, backedBy) } }
)

private fun <T : Any?, BACKED_BY : Any?> AsyncKeyMapper<T, BACKED_BY>.async(context: CoroutineContext) = AsyncKeyMapper<T, BACKED_BY>(
  mapGet = { withContext(context) { mapGet(it) } },
  mapSet = { withContext(context) { mapSet(it) } },
)

private fun <BACKED_BY : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> AsyncKeyBacker<BACKED_BY, GETTER, SETTER>.async(
  context: CoroutineContext,
) = AsyncKeyBacker<BACKED_BY, GETTER, SETTER>(
  getBackingData = { withContext(context) { getBackingData(it) } },
  setBackingData = { setter, backedBy -> withContext(context) { setBackingData(setter, backedBy) } }
)
