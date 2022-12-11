package com.episode6.typed2.gson

import com.episode6.typed2.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

object Typed2DefaultGson {
  private val default: Gson by lazy { Gson() }
  fun gson(): Gson = default
}

inline fun <reified T : Any, BACKED_BY, GETTER : KeyValueGetter, SETTER : KeyValueSetter> Key<String?, BACKED_BY, GETTER, SETTER>.mapGson(
  noinline gson: () -> Gson = Typed2DefaultGson::gson,
): Key<T?, BACKED_BY, GETTER, SETTER> = mapType(
  mapGet = { it?.let { gson().fromJson(it, typeToken<T>().type) } },
  mapSet = { it?.let { gson().toJson(it, typeToken<T>().type) } }
)

inline fun <reified T : Any, BACKED_BY, GETTER : KeyValueGetter, SETTER : KeyValueSetter> AsyncKey<String?, BACKED_BY, GETTER, SETTER>.mapGson(
  context: CoroutineContext = EmptyCoroutineContext,
  noinline gson: () -> Gson = Typed2DefaultGson::gson,
): AsyncKey<T?, BACKED_BY, GETTER, SETTER> = mapType<String?, T?, BACKED_BY, GETTER, SETTER>(
  mapGet = { it?.let { gson().fromJson(it, typeToken<T>().type) } },
  mapSet = { it?.let { gson().toJson(it, typeToken<T>().type) } }
).asyncContext(mapperContext = context)

inline fun <reified T : Any> PrimitiveKeyBuilder.gson(
  default: T,
  noinline gson: () -> Gson = Typed2DefaultGson::gson,
): PrimitiveKey<T, String?> = gson<T>(gson).defaultProvider { default }

inline fun <reified T : Any> PrimitiveKeyBuilder.gson(
  noinline gson: () -> Gson = Typed2DefaultGson::gson,
): PrimitiveKey<T?, String?> = string().mapGson(gson)

inline fun <reified T : Any> AsyncPrimitiveKeyBuilder.gson(
  default: T,
  context: CoroutineContext = EmptyCoroutineContext,
  noinline gson: () -> Gson = Typed2DefaultGson::gson,
): AsyncPrimitiveKey<T, String?> = gson<T>(context, gson).defaultProvider { default }

inline fun <reified T : Any> AsyncPrimitiveKeyBuilder.gson(
  context: CoroutineContext = EmptyCoroutineContext,
  noinline gson: () -> Gson = Typed2DefaultGson::gson,
): AsyncPrimitiveKey<T?, String?> = string().mapGson(context, gson)

inline fun <reified T : Any?> typeToken(): TypeToken<T> = object : TypeToken<T>() {}
