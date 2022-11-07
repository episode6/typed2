package com.episode6.typed2

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext


fun <T : Any, BACKED_BY : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> Key<T?, BACKED_BY, GETTER, SETTER>.defaultProvider(
  default: () -> T,
): Key<T, BACKED_BY, GETTER, SETTER> = withOutputDefault(OutputDefault.Provider(default))

fun <T : Any, BACKED_BY : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> AsyncKey<T?, BACKED_BY, GETTER, SETTER>.defaultProvider(
  default: suspend () -> T,
): AsyncKey<T, BACKED_BY, GETTER, SETTER> = withOutputDefault(OutputDefault.SuspendProvider(default))

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
  default: OutputDefault<T>,
): AsyncKey<T, BACKED_BY, GETTER, SETTER> = AsyncKey(
  name = name,
  backingTypeInfo = backingTypeInfo,
  backer = backer,
  outputDefault = default,
  mapper = AsyncKeyMapper(
    mapSet = mapper.mapSet,
    mapGet = { mapper.mapGet(it) ?: default.suspendProvider().invoke() }
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
  outputDefault = outputDefault?.mapSuspend(mapGet),
  mapper = AsyncKeyMapper(
    mapSet = { mapper.mapSet(mapSet(it)) },
    mapGet = { mapGet(mapper.mapGet(it)) }
  ),
  newKeyCallback = newKeyCallback,
)

fun <T : Any?, BACKED_BY : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> Key<T, BACKED_BY, GETTER, SETTER>.async(
  context: CoroutineContext = Dispatchers.Default,
): AsyncKey<T, BACKED_BY, GETTER, SETTER> = AsyncKey(
  name = name,
  outputDefault = outputDefault,
  backingTypeInfo = backingTypeInfo,
  backer = backer,
  mapper = mapper.async(context),
  newKeyCallback = newKeyCallback,
)

private fun <T : Any?, BACKED_BY : Any?> KeyMapper<T, BACKED_BY>.async(context: CoroutineContext) = AsyncKeyMapper<T, BACKED_BY>(
  mapGet = { withContext(context) { mapGet(it) } },
  mapSet = { withContext(context) { mapSet(it) } },
)
