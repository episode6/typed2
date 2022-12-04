package com.episode6.typed2

import java.math.BigDecimal

typealias AsyncPrimitiveKey<T, BACKED_BY> = AsyncKey<T, BACKED_BY, AsyncPrimitiveKeyValueGetter, AsyncPrimitiveKeyValueSetter>

interface AsyncPrimitiveKeyBuilder : KeyBuilder {
  suspend fun String.encode(): String = this
  suspend fun String.decode(): String = this
}

fun AsyncPrimitiveKeyBuilder.boolean(default: Boolean): AsyncPrimitiveKey<Boolean, Boolean> = nativeAsyncKey(
  get = { getBoolean(name, default) },
  set = { setBoolean(name, it) },
  backingDefault = default
)

fun AsyncPrimitiveKeyBuilder.boolean(): AsyncPrimitiveKey<Boolean?, String?> = string().mapType(
  mapGet = { it?.toBoolean() },
  mapSet = { it?.toString() }
)

fun AsyncPrimitiveKeyBuilder.float(default: Float): AsyncPrimitiveKey<Float, Float> = nativeAsyncKey(
  get = { getFloat(name, default) },
  set = { setFloat(name, it) },
  backingDefault = default
)

fun AsyncPrimitiveKeyBuilder.float(): AsyncPrimitiveKey<Float?, String?> = string().mapType(
  mapGet = { it?.toFloat() },
  mapSet = { it?.toString() }
)

fun AsyncPrimitiveKeyBuilder.int(default: Int): AsyncPrimitiveKey<Int, Int> = nativeAsyncKey(
  get = { getInt(name, default) },
  set = { setInt(name, it) },
  backingDefault = default
)

fun AsyncPrimitiveKeyBuilder.int(): AsyncPrimitiveKey<Int?, String?> = string().mapType(
  mapGet = { it?.toInt() },
  mapSet = { it?.toString() }
)

fun AsyncPrimitiveKeyBuilder.long(default: Long): AsyncPrimitiveKey<Long, Long> = nativeAsyncKey(
  get = { getLong(name, default) },
  set = { setLong(name, it) },
  backingDefault = default
)

fun AsyncPrimitiveKeyBuilder.long(): AsyncPrimitiveKey<Long?, String?> = string().mapType(
  mapGet = { it?.toLong() },
  mapSet = { it?.toString() }
)

fun AsyncPrimitiveKeyBuilder.string(default: String): AsyncPrimitiveKey<String, String> =
  nativeAsyncKey<String, AsyncPrimitiveKeyValueGetter, AsyncPrimitiveKeyValueSetter>(
    get = { getString(name, default) ?: default },
    set = { setString(name, it) },
    backingDefault = default
  ).mapType(
    mapGet = { it.decode() },
    mapSet = { it.encode() },
  )

fun AsyncPrimitiveKeyBuilder.string(): AsyncPrimitiveKey<String?, String?> =
  nativeAsyncKey<String, AsyncPrimitiveKeyValueGetter, AsyncPrimitiveKeyValueSetter>(
    get = { getString(name, null) },
    set = { setString(name, it) },
  ).mapType(
    mapGet = { it?.decode() },
    mapSet = { it?.encode() },
  )

fun AsyncPrimitiveKeyBuilder.double(): AsyncPrimitiveKey<Double?, String?> = string().mapType(
  mapGet = { it?.let { BigDecimal(it).toDouble() } },
  mapSet = { it?.toBigDecimal()?.toPlainString() },
)
