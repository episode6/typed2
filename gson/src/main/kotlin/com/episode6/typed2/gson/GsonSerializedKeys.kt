package com.episode6.typed2.gson

import com.episode6.typed2.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Typed2DefaultGson {
  private val default: Gson by lazy(LazyThreadSafetyMode.PUBLICATION) { Gson() }
  fun gson(): Gson = default
}

inline fun <reified T : Any> PrimitiveKeyBuilder.gson(
  default: T,
  noinline gson: () -> Gson = Typed2DefaultGson::gson,
): PrimitiveKey<T, String?> = gson<T>(gson).withDefault { default }

inline fun <reified T : Any> PrimitiveKeyBuilder.gson(
  noinline gson: () -> Gson = Typed2DefaultGson::gson,
  noinline default: ()->T,
): PrimitiveKey<T, String?> = gson<T>(gson).withDefault(default)

inline fun <reified T : Any> PrimitiveKeyBuilder.gson(
  noinline gson: () -> Gson = Typed2DefaultGson::gson,
): PrimitiveKey<T?, String?> = string().mapType(
  mapGet = { it?.let { gson().fromJson(it, typeToken<T>().type) } },
  mapSet = { it?.let { gson().toJson(it, typeToken<T>().type) } }
)

inline fun <reified T : Any?> typeToken(): TypeToken<T> = object : TypeToken<T>() {}
