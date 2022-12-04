package com.episode6.typed2

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

interface AsyncMappingInlineBackingNamespace {
  fun <T : Any?, BACKED_BY : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> Key<T, BACKED_BY, GETTER, SETTER>.async(
    context: CoroutineContext = Dispatchers.Default,
  ): AsyncKey<T, BACKED_BY, GETTER, SETTER> = async(mapperContext = context, backerContext = EmptyCoroutineContext)
}
